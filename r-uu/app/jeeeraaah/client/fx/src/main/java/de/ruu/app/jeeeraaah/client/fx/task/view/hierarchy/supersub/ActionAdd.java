package de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.supersub;

import de.ruu.app.jeeeraaah.client.fx.task.edit.TaskEditor;
import de.ruu.app.jeeeraaah.client.rs.TaskServiceClient;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ActionAdd
{
	@Getter @Accessors(fluent = true)
	static class Context
	{
		private TreeView<TaskBean> treeView;
		private TaskGroupBean      taskGroup;
		private TaskEditor         taskEditor;
		private TaskServiceClient  taskServiceClient;

		Context
				(
						@NonNull TreeView<TaskBean> treeView,
						@NonNull TaskGroupBean      taskGroup,
						@NonNull TaskEditor         taskEditor,
						@NonNull TaskServiceClient  taskServiceClient
				)
		{
			this.treeView          = treeView;
			this.taskGroup         = taskGroup;
			this.taskEditor        = taskEditor;
			this.taskServiceClient = taskServiceClient;
		}
	}

	private final Context context;

	ActionAdd(@NonNull Context context) { this.context = context; }

	void execute()
	{
		TreeItem<TaskBean> selectedItem = context.treeView().getSelectionModel().getSelectedItem();

		if (selectedItem == null) new ActionAddMainTask     (context).execute();
		else                      new ActionAddMainOrSubTask(context).execute();
	}
}