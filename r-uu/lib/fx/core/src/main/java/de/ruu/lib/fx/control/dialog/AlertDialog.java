package de.ruu.lib.fx.control.dialog;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

public abstract class AlertDialog
{
	public static void showAndWait(String content)                 { showAndWait(content, AlertType.INFORMATION); }
	public static void showAndWait(String content, AlertType type) { showAndWait(""     , "", content, type); }
	public static void showAndWait(String title, String content)   { showAndWait(title  , "", content, AlertType.INFORMATION); }

	public static void showAndWait(String title, String header, String content, AlertType type)
	{
		Alert alert = new Alert(type);
		alert.setContentText(content);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setResizable(true);
		alert.getDialogPane().setMinHeight(Region.USE_COMPUTED_SIZE);
		alert.showAndWait();
	}
}