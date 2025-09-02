package de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor.add;

import de.ruu.app.jeeeraaah.client.fx.task.selector.TaskSelector;
import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor.add.ActionAdd.Context;
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
 * <code>ActionAddToSuperSub</code> is responsible for adding a task as a new successor task to the selected super/sub
 * task.
 * <p>
 * It creates a dialog for the user to choose the receiver of the successor task, and upon confirmation, it creates a
 * new assignment between the receiver and its successor in the database.
 */
public class ActionAddToSuperSub
{
	private final Context context;

	private final TaskSelector taskSelector = CDI.current().select(TaskSelector.class).get();

	public ActionAddToSuperSub(@NonNull Context context) { this.context = context; }

	public void execute()
	{
		taskSelector.localRoot(); // ensure that the task selector is initialized
		taskSelector.service().populateFor(context.treeItemSelectedSuperSubTask().getValue().taskGroup());

		Dialog<TaskBean> dialog = new Dialog<>();
		dialog.setTitle("choose target task for new successor relation");
		dialog.setResultConverter(this::dialogResultConverter);

		DialogPane pane = dialog.getDialogPane();
		pane.setContent(taskSelector.localRoot());
		pane.getButtonTypes().addAll(CANCEL, OK);

		Optional<TaskBean> optional = dialog.showAndWait();

		optional.ifPresent
		(
				chosenSuccessorTaskBean ->
				{
					try
					{
						context.taskServiceClient().addSuccessor
						(
								context.treeItemSelectedSuperSubTask().getValue().toDTO(new ReferenceCycleTracking()),
								chosenSuccessorTaskBean                          .toDTO(new ReferenceCycleTracking())
						);
						TreeItem<TaskBean> newItemInSuccessorTree = new TreeItem<>(chosenSuccessorTaskBean);
						context.treeItemSelectedSuccessorTask().getChildren().add(newItemInSuccessorTree);
						populateTreeForSuccessorsOf(newItemInSuccessorTree);
						// add the chosen predecessor task to the predecessor tasks of the selected super/sub task
						context.treeItemSelectedSuperSubTask().getValue().addSuccessor(chosenSuccessorTaskBean);
					}
					catch (TaskRelationException e)
					{
						AlertDialog.showAndWait
						(
								"error adding successor relation",
								"An error occurred while adding the successor relation:\n" + e.message(),
								"Please check the task selection and try again.",
								ERROR
						);
					}
				}
		);
	}

	private void populateTreeForSuccessorsOf(TreeItem<TaskBean> newItemInSuccessorTree)
	{
		TaskBean successorTask = newItemInSuccessorTree.getValue();

		successorTask.successors().ifPresent
				(
						successors ->
								successors.forEach
										(
												successor ->
												{
													TreeItem<TaskBean> successorOfNewItemInSucccessorTree = new TreeItem<>(successor);
													newItemInSuccessorTree.getChildren().add(successorOfNewItemInSucccessorTree);
													populateTreeForSuccessorsOf(successorOfNewItemInSucccessorTree);
												}
										)
				);
	}

	private TaskBean dialogResultConverter(ButtonType btn)
	{
		return taskSelector.service().selectionChangedProperty().get();
	}
}