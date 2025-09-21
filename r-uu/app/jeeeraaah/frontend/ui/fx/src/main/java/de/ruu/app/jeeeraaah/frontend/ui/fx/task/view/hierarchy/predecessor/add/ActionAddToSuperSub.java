package de.ruu.app.jeeeraaah.frontend.ui.fx.task.view.hierarchy.predecessor.add;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.frontend.ui.fx.task.selector.TaskSelector;
import de.ruu.app.jeeeraaah.frontend.ui.fx.task.view.hierarchy.predecessor.add.ActionAdd.Context;
import de.ruu.lib.fx.control.dialog.ExceptionDialog;
import de.ruu.lib.ws.rs.NonTechnicalException;
import de.ruu.lib.ws.rs.TechnicalException;
import jakarta.enterprise.inject.spi.CDI;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TreeItem;
import lombok.NonNull;

import java.util.Optional;

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

		if (optional.isPresent())
		{
			// fetch new task bean from dialog
			TaskBean chosenPredecessorTaskBean = optional.get();

			// fetch the selected super/sub task from the tree view
			TaskBean superSubTask = context.treeItemSelectedSuperSubTask().getValue();

			try
			{
				// let task service client create a new item in the backend
				context.taskServiceClient().addPredecessor(superSubTask, chosenPredecessorTaskBean);

				TreeItem<TaskBean> newItemInPredecessorTree = new TreeItem<>(chosenPredecessorTaskBean);

				// update the context
				context.treeItemSelectedPredecessorTask().getChildren().add(newItemInPredecessorTree);

				// update the tree view
				populateTreeForPredecessorsOf(newItemInPredecessorTree);

				// add the chosen predecessor task to the predecessor tasks of the selected super/sub task
				superSubTask.addPredecessor(chosenPredecessorTaskBean);
			}
			catch (TechnicalException | NonTechnicalException e)
			{
				ExceptionDialog.showAndWait
				(
						"failure creating new subtask relation",
						"the predecessor relation between\n" + superSubTask + "\nand\n" +
								chosenPredecessorTaskBean + "\ncould not be created in the backend",
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

	private TaskBean dialogResultConverter(ButtonType btn)
	{
		return taskSelector.service().selectionChangedProperty().get();
	}
}