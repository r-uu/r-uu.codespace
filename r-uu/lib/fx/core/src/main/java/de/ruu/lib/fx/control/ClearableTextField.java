package de.ruu.lib.fx.control;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static javafx.scene.layout.Priority.ALWAYS;

public class ClearableTextField extends StackPane
//public class ClearableTextField extends HBox
{
    private final TextField textField   = new TextField();
    private final Button    clearButton = new Button("✕");

    public ClearableTextField() { setup(); }

    private void setup()
    {
        // clearButton properties
        clearButton.setFocusTraversable(false);
        clearButton.setBackground(Background.EMPTY);
        clearButton.setTextFill(Color.GRAY);
        clearButton.setFont(Font.font(null, FontWeight.BOLD, Font.getDefault().getSize()));
        clearButton.setOnAction(e -> textField.clear());
        clearButton.visibleProperty().bind(Bindings.isNotEmpty(textField.textProperty()));

        StackPane.setAlignment(clearButton, Pos.CENTER_RIGHT);
        StackPane.setMargin   (clearButton, new Insets(0, 2, 0, 0));
//        HBox     .setMargin   (clearButton, new Insets(0, 2, 0, 0));

        // textField properties
        textField.setMaxWidth(Double.MAX_VALUE);

        setMaxWidth(Double.MAX_VALUE);
//        setSpacing(2);
        setPadding(new Insets(0, 2, 0, 2));

        HBox.setHgrow(this, ALWAYS);
        HBox.setHgrow(textField, ALWAYS);

        // layout
        getChildren().addAll(textField, clearButton);
    }

    public TextField textField()           { return textField;                       }
    public String    text()                { return textField.getText();             }
    public void      text  (String text)   {        textField.setText(text);         }
    public void      prompt(String prompt) {        textField.setPromptText(prompt); }
}