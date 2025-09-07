package de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor.add;

import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor.add.ActionAdd.Context;
import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor.add.super_sub_or_predecessor.Configurator;
import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor.add.super_sub_or_predecessor.ConfiguratorService.ActionAddToSuperSubOrPredecessorConfigurationResult;
import de.ruu.lib.ws.rs.NonTechnicalException;
import de.ruu.lib.ws.rs.TechnicalException;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.lib.fx.control.dialog.ExceptionDialog;
import jakarta.enterprise.inject.spi.CDI;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.Region;
import lombok.NonNull;

import java.util.Optional;

import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * <code>ActionAddToSuperSubOrPredecessor</code> is responsible for adding a task as a new predecessor task to either
 * the selected predecessor or the selected super/sub task.
 * <p>
 * It creates a dialog for the user to choose the receiver of the predecessor task, and upon confirmation, it creates a
 * new assignment between the receiver and its predecessor in the database.
 */
class ActionAddToSuperSubOrPredecessor
{
	private final Context context;

	private final Configurator configurator = CDI.current().select(Configurator.class).get();

	ActionAddToSuperSubOrPredecessor(@NonNull Context context) { this.context = context; }

	void execute()
	{
		Parent configuratorLocalRoot = configurator.localRoot();// ensure that the configurator is initialized
		configurator.service().context(context);

		Dialog<ActionAddToSuperSubOrPredecessorConfigurationResult> dialog = new Dialog<>();
		dialog.setTitle("choose target task (super/sub or predecessor) for new predecessor relation");
		dialog.setResultConverter(this::dialogResultConverter);
		dialog.setResizable(true);

		DialogPane pane = dialog.getDialogPane();
		pane.setContent(configuratorLocalRoot);
		pane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
		pane.getButtonTypes().addAll(CANCEL, OK);

		dialog.setOnShown(e -> pane.getScene().getWindow().sizeToScene()); // make sure window resizes correctly

		Optional<ActionAddToSuperSubOrPredecessorConfigurationResult> optional = dialog.showAndWait();

		if (optional.isPresent())
		{
			// fetch the configuration result from dialog
			ActionAddToSuperSubOrPredecessorConfigurationResult configurationResult = optional.get();

			TaskBean targetTask    = configurationResult.targetTask();
			TaskBean predecessorTask = configurationResult.chosenPredecessorTaskBean();

			try
			{
				// let the task service add the predecessor relation
				context.taskServiceClient().addPredecessor(targetTask, predecessorTask);

				TreeItem<TaskBean> newItemInPredecessorTree = new TreeItem<>(configurationResult.chosenPredecessorTaskBean());

				// update the context
				context.treeItemSelectedPredecessorTask().getChildren().add(newItemInPredecessorTree);

				// update the tree
				populateTreeForPredecessorsOf(newItemInPredecessorTree);

				// add the predecessor task to the target task
				targetTask.addPredecessor(predecessorTask);
			}
			catch (TechnicalException | NonTechnicalException e)
			{
				ExceptionDialog.showAndWait
						(
								"failure creating new predecessor relation",
								"the predecessor relation between\n" + targetTask + "\nand\n" +
										predecessorTask + "\ncould not be created in the backend",
								e.getMessage(),
								e
						);
			}
		}
	}

	private void populateTreeForPredecessorsOf(TreeItem<TaskBean> newItemInPredecessorTree)
	{
		TaskBean predecessorTask = newItemInPredecessorTree.getValue();

		predecessorTask.predecessors().ifPresent
				(
						predecessors ->
								predecessors.forEach
										(
												predecessor ->
												{
													TreeItem<TaskBean> predecessorOfNewItemInPredecessorTree = new TreeItem<>(predecessor);
													newItemInPredecessorTree.getChildren().add(predecessorOfNewItemInPredecessorTree);
													populateTreeForPredecessorsOf(predecessorOfNewItemInPredecessorTree);
												}
										)
				);
	}

	private ActionAddToSuperSubOrPredecessorConfigurationResult dialogResultConverter(ButtonType btn)
	{
		if (btn.getButtonData() == OK_DONE)
				if (configurator.service().result().isPresent())
						return new ActionAddToSuperSubOrPredecessorConfigurationResult
						(
								configurator.service().result().get().chosenPredecessorTaskBean(),
								configurator.service().result().get().targetTask()
						);
		return null;
	}
}