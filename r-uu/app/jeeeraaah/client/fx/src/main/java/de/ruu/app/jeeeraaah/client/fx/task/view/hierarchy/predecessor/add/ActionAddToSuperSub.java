package de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor.add;

import de.ruu.app.jeeeraaah.client.fx.task.selector.TaskSelector;
import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor.add.ActionAdd.Context;
import de.ruu.app.jeeeraaah.common.TaskRelationException;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.lib.fx.control.dialog.AlertDialog;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import jakarta.enterprise.inject.spi.CDI;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TreeItem;
import lombok.NonNull;

import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

/**
 * <code>ActionAddToSuperSub</code> is responsible for adding a task as a new predecessor task to the selected super/sub
 * task.
 * <p>
 * It creates a dialog for the user to choose the receiver of the predecessor task, and upon confirmation, it creates a
 * new assignment between the receiver and its predecessor in the database.
 */
class ActionAddToSuperSub
{
	private final Context context;

	private final TaskSelector taskSelector = CDI.current().select(TaskSelector.class).get();

	public ActionAddToSuperSub(@NonNull Context context) { this.context = context; }

	public void execute()
	{
		taskSelector.localRoot(); // ensure that the task selector is initialized
		taskSelector.service().populateFor(context.treeItemSelectedSuperSubTask().getValue().taskGroup());

		Dialog<TaskBean> dialog = new Dialog<>();
		dialog.setTitle("choose target task for new predecessor relation");
		dialog.setResultConverter(this::dialogResultConverter);

		DialogPane pane = dialog.getDialogPane();
		pane.setContent(taskSelector.localRoot());
		pane.getButtonTypes().addAll(CANCEL, OK);

		Optional<TaskBean> optional = dialog.showAndWait();

		optional.ifPresent
		(
				chosenPredecessorTaskBean ->
				{
					try
					{
						context.taskServiceClient().addPredecessor
						(
								context.treeItemSelectedSuperSubTask().getValue().toDTO(new ReferenceCycleTracking()),
								chosenPredecessorTaskBean                        .toDTO(new ReferenceCycleTracking())
						);
						TreeItem<TaskBean> newItemInPredecessorTree = new TreeItem<>(chosenPredecessorTaskBean);
						context.treeItemSelectedPredecessorTask().getChildren().add(newItemInPredecessorTree);
						populateTreeForPredecessorsOf(newItemInPredecessorTree);
						// add the chosen predecessor task to the predecessor tasks of the selected super/sub task
						context.treeItemSelectedSuperSubTask().getValue().addPredecessor(chosenPredecessorTaskBean);
					}
					catch (TaskRelationException e)
					{
						AlertDialog.showAndWait
						(
								"error adding predecessor relation",
								"An error occurred while adding the predecessor relation:\n" + e.message(),
								"Please check the task selection and try again.",
								ERROR
						);
					}
				}
		);
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

	private TaskBean dialogResultConverter(ButtonType btn)
	{
		return taskSelector.service().selectionChangedProperty().get();
	}
}