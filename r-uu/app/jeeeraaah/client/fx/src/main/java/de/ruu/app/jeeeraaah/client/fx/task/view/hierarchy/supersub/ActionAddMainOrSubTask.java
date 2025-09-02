package de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.supersub;

import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.supersub.ActionAdd.Context;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.fx.TaskFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import lombok.NonNull;

import java.util.Optional;

import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;
import static javafx.scene.control.ButtonType.CANCEL;
import static javafx.scene.control.ButtonType.OK;

class ActionAddMainOrSubTask
{
	private final Context context;

	ActionAddMainOrSubTask(@NonNull Context context) { this.context = context; }

	void execute()
	{
		Dialog<TaskFXBean> dialog            = new Dialog<>();
		DialogPane         dialogPane        = dialog.getDialogPane();
		VBox               dialogPaneContent = new VBox();
		CheckBox           checkBox          = new CheckBox("create main task for task group");

		dialog           .setTitle("new main or sub task");
		dialog           .setHeaderText("create a new main task for the task group or a new sub task for the selected task");
		dialog           .setResultConverter(this::dialogResultConverterFXBean);
		dialogPane       .getButtonTypes().addAll(CANCEL, OK);
		dialogPane       .setContent(dialogPaneContent);
		dialogPaneContent.getChildren().add(context.taskEditor().localRoot());
		dialogPaneContent.getChildren().add(checkBox);
		checkBox         .setSelected(false);

		// create a transient task and put it into the editor
		TaskBean   taskBeanTransient = new TaskBean(context.taskGroup(), "new main or sub task");
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
			// - add the new tree item for the task as main or sub task to the tree view depending on the checkbox
			if (checkBox.isSelected())
					// if the user has checked the checkbox, we create a main task in the tree
					context.treeView().getRoot().getChildren().add(taskBeanTreeItemNew);
			else
			{
				// otherwise we create a sub task of the currently selected item in the tree as well as in the selected task
				// itself
				context.treeView().getSelectionModel().getSelectedItem().getChildren().add(taskBeanTreeItemNew);

				// we also have to add the sub task relation to the currently selected task in the db
				context
						.taskServiceClient()
						.addSubTask(
								context
										.treeView()
										.getSelectionModel()
										.getSelectedItem()
										.getValue()
										.toDTO(new ReferenceCycleTracking()),
								taskEntityDTOPersisted);
			}

			// update the context
			// - remove the transient task from the context task group
			context.taskGroup().removeTask(taskBeanTransient);
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