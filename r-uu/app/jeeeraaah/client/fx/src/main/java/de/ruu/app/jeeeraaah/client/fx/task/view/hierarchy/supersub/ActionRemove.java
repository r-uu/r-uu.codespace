package de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.supersub;

import de.ruu.app.jeeeraaah.client.rs.TaskGroupServiceClient;
import de.ruu.app.jeeeraaah.client.rs.TaskServiceClient;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.lib.fx.control.dialog.AlertDialog;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import static de.ruu.lib.util.BooleanFunctions.not;
import static javafx.scene.control.Alert.AlertType.ERROR;

@Slf4j
class ActionRemove
{
	@Getter @Accessors(fluent = true)
	static class Context
	{
		private TreeView<TaskBean>     superSubTreeView;
		private TaskGroupBean          activeTaskGroup;
		private TaskGroupServiceClient taskGroupServiceClient;
		private TaskServiceClient      taskServiceClient;

		Context
				(
						@NonNull TreeView<TaskBean>     superSubTreeView,
						@NonNull TaskGroupBean          activeTaskGroup,
						@NonNull TaskGroupServiceClient taskGroupServiceClient,
						@NonNull TaskServiceClient      taskServiceClient
				)
		{
			this.superSubTreeView       = superSubTreeView;
			this.activeTaskGroup        = activeTaskGroup;
			this.taskGroupServiceClient = taskGroupServiceClient;
			this.taskServiceClient      = taskServiceClient;
		}
	}

	private final Context context;

	ActionRemove(@NonNull Context context) { this.context = context; }

	void execute()
	{
		TreeItem<TaskBean> selectedItem = context.superSubTreeView().getSelectionModel().getSelectedItem();
		TaskBean           selectedTask = selectedItem.getValue();

		if (selectedTask.predecessors().isPresent() && not(selectedTask.predecessors().get().isEmpty()))
		{
			AlertDialog.showAndWait(
					"cannot remove task",
					"the selected task cannot be removed, because it has predecessor relations",
					"remove the predecessor relations first, then you can remove this task",
					ERROR
			);
			return;
		}

		if (selectedTask.successors().isPresent() && not(selectedTask.successors().get().isEmpty()))
		{
			AlertDialog.showAndWait(
					"cannot remove task",
					"the selected task cannot be removed, because it has successor relations",
					"remove the successor relations first, then you can remove this task",
					ERROR
			);
			return;
		}

		if (selectedTask.subTasks().isPresent() && not(selectedTask.subTasks().get().isEmpty()))
		{
			AlertDialog.showAndWait(
					"cannot remove task",
					"the selected task cannot be removed, because it has sub tasks",
					"remove the sub tasks first, then you can remove this task",
					ERROR
			);
			return;
		}

		if (selectedTask.superTask().isPresent())
		{
			// preparation
			TaskBean               superTask              = selectedTask.superTask().get();
			ReferenceCycleTracking referenceCycleTracking = new ReferenceCycleTracking();

			// remove the super-sub relation in the db
			context
					.taskServiceClient
					.removeSuperSubTask(superTask.toDTO(referenceCycleTracking), selectedTask.toDTO(referenceCycleTracking));
			// remove the super-sub relation in the current client-side beans
			// - remove the relation of the client-side task from the client-side super task
			superTask.removeSubTask(selectedTask);
			// - remove the task from the current client-side group
			boolean removeTaskFromActiveTaskGroupResult = context.activeTaskGroup.removeTask(selectedTask);
			// remove the super-sub relation in the tree view
			TreeItem<TaskBean> parent = selectedItem.getParent();
			if (parent != null) parent.getChildren().remove(selectedItem);
		}
		else
		{
			if (context.activeTaskGroup.id() != null && selectedTask.id() != null)
			{
				// remove the super-sub relation in the db
				if (context.taskGroupServiceClient.removeFromGroup(context.activeTaskGroup().id(), selectedTask.id()))
				{
					// removal of task from active task group was successful, remove the task in the ui
					// - remove the task from the current client-side group
					boolean removeTaskFromActiveTaskGroupResult = context.activeTaskGroup().removeTask(selectedTask);
					// - remove the task from the tree view (from the root, because it is a main task)
					TreeItem<TaskBean> parent = selectedItem.getParent();
					if (parent != null) parent.getChildren().remove(selectedItem);
				}
				else
						AlertDialog.showAndWait(
								"failure removing task",
								"the selected task might still be in context task group",
								"the selected task could not be removed from the context task group",
								ERROR);
			}
			else
					throw new IllegalStateException("active task group and selected task must have an id");
		}
	}
}