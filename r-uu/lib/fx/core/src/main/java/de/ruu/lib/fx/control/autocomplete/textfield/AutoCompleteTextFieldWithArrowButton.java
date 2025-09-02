package de.ruu.lib.fx.control.autocomplete.textfield;

import de.ruu.lib.util.Strings;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.StackLocatorUtil;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static de.ruu.lib.util.BooleanFunctions.not;

@Slf4j
public class AutoCompleteTextFieldWithArrowButton<T> extends AutoCompleteTextField<T>
{
	public AutoCompleteTextFieldWithArrowButton
	(
			         List       <T         > items,
			@NonNull BiPredicate<T, String > suggestionFilter,
			         Comparator <T         > comparator,
			         Function   <T, Node   > graphicsProvider,
			         Function   <T, String > textProvider,
			         Function   <T, Tooltip> toolTipProvider,
			         String                  promptText
	)
	{
		super(items, suggestionFilter, comparator/*, converterTest*/, graphicsProvider, textProvider, toolTipProvider, promptText);

//		Polygon arrow = new Polygon(0, 0, 10, 0, 5, 7);
//		arrow.setFill(Color.DIMGRAY);

//		StackPane arrowPane = new StackPane(arrow);
//		arrowPane.setPrefSize(15, 10);

		Button arrowButton = new Button("▼");
		arrowButton.setFont(new Font(10));
//		Button arrowButton = new Button();
//		arrowButton.setGraphic(arrowPane);
		arrowButton.setFocusTraversable(false);
//		arrowButton.setMinSize(30, 30);
//		arrowButton.setMaxSize(30, 30);
		arrowButton.setPrefSize(25, 25);
//		// make sure the button is not visible (and cannot be clicked) when the list of suggestions is empty
//		arrowButton
//				.visibleProperty()
//				.bind(Bindings.createBooleanBinding(() -> textFieldIsEmptyButSuggestionsAreAvailable()));
		arrowButton.setOnAction(e -> onArrowButtonClicked());

		arrowButton.setBackground(Background.EMPTY);
		arrowButton.setBorder    (Border.EMPTY);
//		arrowButton.setPadding(Insets.EMPTY);
		setSpacing(0);
//		getChildren().add(arrowPane);
		getChildren().add(arrowButton);
//		setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
	}

//	private boolean textFieldIsEmptyButSuggestionsAreAvailable()
//	{
//		boolean result = false;
//		if (Strings.isEmptyOrBlank(textField().getText()) && items())
//		{
//			log.warn("text field or list view is null, cannot determine if suggestions are available.");
//			return false;
//		}
//		return not(listView.itemsProperty().get().isEmpty());
//	}

	private void onArrowButtonClicked() { showPopup(); }
}