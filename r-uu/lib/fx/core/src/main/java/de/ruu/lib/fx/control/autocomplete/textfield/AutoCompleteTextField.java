package de.ruu.lib.fx.control.autocomplete.textfield;

import de.ruu.lib.fx.control.ClearableTextField;
import de.ruu.lib.fx.control.autocomplete.AutoCompleteCellFactory;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.ruu.lib.util.BooleanFunctions.not;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
//public class AutoCompleteTextField<T> extends ClearableTextField
public class AutoCompleteTextField<T> extends HBox
{
	private   final ClearableTextField clearableTextField = new ClearableTextField();
	protected final ListView<T>        listView           = new ListView<>();
	private   final Popup              popup              = new Popup();

	/** stores the current value of an instance at all time, accessible for clients via {@link #valueProperty()} */
	private final ObjectProperty<T> value = new SimpleObjectProperty<>();

	private final ObservableList<T         > items;
	private final BiPredicate   <T, String > suggestionFilter;
	private final Comparator    <T         > comparator;
	private final Function      <T, Node   > graphicsProvider;
	private final Function      <T, String > textProvider;
	private final Function      <T, Tooltip> toolTipProvider;

	// convenience reference (shortcut) to the text field of the clearable text field
	private final TextField textField = clearableTextField.textField();

	/**
	 * @param suggestionFilter returns <code>true</code> if an item is a valid suggestion for the value of the combo box,
	 *        <code>false</code> otherwise
	 * @param graphicsProvider returns a <code>Node</code> to be used as graphic for the item in the list view
	 * @param textProvider     returns a <code>String</code> to be used as text for the item in the list view
	 * @param toolTipProvider  returns a <code>Tooltip</code> to be used for the item in the list view
	 * @param promptText       the text to be shown in the text field when it is empty
	 */
	public AutoCompleteTextField
	(
			List                <T         > items,
			@NonNull BiPredicate<T, String > suggestionFilter,
			Comparator          <T         > comparator,
			Function            <T, Node   > graphicsProvider,
			Function            <T, String > textProvider,
			Function            <T, Tooltip> toolTipProvider,
			String                           promptText
	)
	{
		if (isNull(items           )) items            = new ArrayList<>();
		if (isNull(comparator      )) comparator       = (t1, t2) -> 0; // default comparator that does nothing
		if (isNull(graphicsProvider)) graphicsProvider = item -> null;
		if (isNull(textProvider    )) textProvider     = item -> isNull(item) ? "" : item.toString();
		if (isNull(toolTipProvider )) toolTipProvider  = item -> null;
		if (isNull(promptText      )) promptText       = "";

		this.items            = FXCollections.observableArrayList(items);
		this.suggestionFilter = suggestionFilter;
		this.comparator       = comparator;
		this.graphicsProvider = graphicsProvider;
		this.textProvider     = textProvider;
		this.toolTipProvider  = toolTipProvider;

		value.addListener((obs, old, cur) -> onValueChanged(cur));

		textField.setPromptText(promptText);

		setupPopup();
		setupListeners();

		getChildren().add(clearableTextField);
	}

	public void value(final T value)
	{
		if      (value == null)         this.value.set(value);
		else if (items.contains(value)) this.value.set(value);
		else                            log.warn("value not contained in items, did not set value to " + value);
	}
	public ObjectProperty<T> valueProperty() { return value; }

	public void items(List<T> items) { this.items.setAll(items); }

	public T selectedItem() { return listView.getSelectionModel().getSelectedItem(); }

//	public TextField textField() { return textField; }

	//	private void onValueChanged(final T newValue) { textField().setText(converter.toString(newValue)); }
	private void onValueChanged(final T newValue) { clearableTextField.textField().setText(textProvider.apply(newValue)); }

	private void setupPopup()
	{
		listView.setMaxHeight(150);
		listView.setPrefWidth(200);
//		listView.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> textField().getPrefWidth()));
//		listView.prefWidthProperty().bind(textField().prefWidthProperty());
		listView.setFocusTraversable(false);
		listView.setCellFactory(new AutoCompleteCellFactory<>(graphicsProvider, textProvider, toolTipProvider));

		popup.setAutoHide    (true);
		popup.setAutoFix     (true);
		popup.setHideOnEscape(true);
		popup.getContent().add(listView);
	}

	private void setupListeners()
	{
		textField.textProperty().addListener((obs, old, act) -> textFieldListener(act));

		textField.setOnKeyPressed
		(
				event ->
				{
					if (popup.isShowing())
					{
						if (event.getCode() == KeyCode.DOWN)
						{
							listView.requestFocus();
							listView.getSelectionModel().selectFirst();
							event.consume();
						}
					}
					else
					{
//						log.debug("text field key pressed when popup is hidden: {}", event.getCode());
						if (event.getCode() == KeyCode.DOWN)
						{
							populatePopup();
							showPopup();
//							listView.requestFocus();
//							listView.getSelectionModel().selectFirst();
							event.consume();
						}
					}
				}
		);

		listView.setOnKeyPressed
		(
				event ->
				{
					if (event.getCode() == KeyCode.ENTER)
					{
						applySelection();
					}
					else if (event.getCode() == KeyCode.ESCAPE)
					{
						popup.hide();
					}
				}
		);

		listView.setOnMouseClicked
		(
				event ->
				{
					if (event.getClickCount() == 1)
					{
						applySelection();
					}
				}
		);

		focusedProperty().addListener((obs, wasFocused, isNowFocused) -> { if (not(isNowFocused)) popup.hide(); } );
	}

	private void textFieldListener(String act)
	{
		if (isNull(act) || act.isEmpty())
		{
			popup.hide();
			value(null);
			return;
		}

		// there is some text in the text field
		populatePopup();
		showPopup();
	}

	private void populatePopup()
	{
		Set<T> filteredItems =
				items.stream().filter(item -> suggestionFilter.test(item, textField.getText())).collect(Collectors.toSet());
		listView.getItems().setAll(filteredItems.stream().sorted(comparator).toList());
	}

	private void applySelection()
	{
		T selected = listView.getSelectionModel().getSelectedItem();
		if (nonNull(selected))
		{
			String value = textProvider.apply(selected);
			textField.setText(value);
			textField.positionCaret(value.length());
			popup.hide();
			value(selected);
		}
	}

	protected void showPopup()
	{
		// Preselect first item
		Platform.runLater
		(
				() ->
				{
					listView.getSelectionModel().selectFirst();
					listView.scrollTo(0);
					listView.requestFocus();
				}
		);
		Bounds bounds = textField.localToScreen(textField.getBoundsInLocal());
		popup.show(textField, bounds.getMinX(), bounds.getMaxY());
	}
}