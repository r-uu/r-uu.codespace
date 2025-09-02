package de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.supersub;

import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.supersub.ActionAdd.Context;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.fx.TaskFXBean;
import de.ruu.lib.fx.control.dialog.AlertDialog;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TreeItem;
import lombok.NonNull;

import java.util.Optional;

import static de.ruu.lib.util.BooleanFunctions.not;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

class ActionAddMainTask
{
	private final Context context;

	ActionAddMainTask(@NonNull Context context) { this.context = context; }

	void execute()
	{
		Dialog<TaskFXBean> dialog     = new Dialog<>();
		DialogPane         dialogPane = dialog.getDialogPane();

		dialog    .setTitle("new main task");
		dialog    .setHeaderText("create a new main task for the active task group");
		dialog    .setResultConverter(this::dialogResultConverterFXBean);
		dialogPane.getButtonTypes().addAll(CANCEL, OK);
		dialogPane.setContent(context.taskEditor().localRoot());

		// create a transient task and put it into the editor
		TaskBean   taskBeanTransient = new TaskBean(context.taskGroup(), "new main task");
		TaskFXBean taskFXBean        = taskBeanTransient.toFXBean(new ReferenceCycleTracking());
		context.taskEditor().service().task(taskFXBean);

		Optional<TaskFXBean> optional = dialog.showAndWait();

		if (optional.isPresent())
		{
			// fetch new task bean from editor
			TaskBean taskBeanFromEditor = optional.get().toBean(new ReferenceCycleTracking());

			// update the db
			// - create a transient task dto from task bean
			TaskEntityDTO taskEntityDTOTransient = taskBeanFromEditor.toDTO(new ReferenceCycleTracking());
			// - let task service client create a new item in db
			TaskEntityDTO taskEntityDTOPersisted = context.taskServiceClient().create(taskEntityDTOTransient);

			// update the tree view
			// - create a task bean from the persisted task dto
			TaskBean           taskBeanPersisted   = taskEntityDTOPersisted.toBean(new ReferenceCycleTracking());
			// - create a new tree item for the new main task
			TreeItem<TaskBean> taskBeanTreeItemNew = new TreeItem<>(taskBeanPersisted);
			// - add the new tree item with new main task to the root item of the tree view
			context.treeView().getRoot().getChildren().add(taskBeanTreeItemNew);

			// update the context
			// - remove the transient task from the context task group
			boolean removeTaskResult = context.taskGroup().removeTask(taskBeanTransient);
			if (not(removeTaskResult))
			{
				AlertDialog.showAndWait(
						"failure removing transient task",
						"transient task might still be in context task group",
						"the transient task could not be removed from the context task group",
						ERROR);
			}
			// - set the task group of the persistent task to the context task group, thereby the context task group receives
			//   the new persistent task as item of its tasks
			taskBeanPersisted.taskGroup(context.taskGroup());
		}
	}

	private TaskFXBean dialogResultConverterFXBean(ButtonType btn)
	{
		if (btn.getButtonData() == OK_DONE) return context.taskEditor().service().task().orElse(null);
		return null;
	}
}