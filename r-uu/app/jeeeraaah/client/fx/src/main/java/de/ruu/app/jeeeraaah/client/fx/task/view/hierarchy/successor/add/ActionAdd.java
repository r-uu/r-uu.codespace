package de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.successor.add;

import de.ruu.app.jeeeraaah.client.ws.rs.TaskServiceClient;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.lib.fx.control.dialog.AlertDialog;
import javafx.scene.control.TreeItem;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

import static de.ruu.lib.fx.control.treeview.TreeViewUtil.isRoot;
import static de.ruu.lib.util.BooleanFunctions.not;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

/**
 * <code>TaskHierarchySuccessorsController</code> creates <code>ActionAdd</code> instances and invokes their #execute()
 * Action to add a new successor relation to the selected successor or super/sub task.
 * <p>
 * This action determines the context of the selected successor tree item and the selected super/sub tree item,
 * and delegates the creation of a new successor-predecessor relation accordingly.
 */
public class ActionAdd
{
	/**
	 * Context for the {@link ActionAdd} action, reflects the relevant aspects of the current user interface status and
	 * provides a {@link TaskServiceClient} instance to be used for persistence access.
	 * <p>
	 * Contains
	 * <ul>
	 *   <li>the selected super/sub task tree item (or the invisible root tree item from super/sub tree view) and</li>
	 *   <li>the selected successor tree item (or the invisible root tree item from successor tree view),</li>
	 *   <li>the task service client.</li>
	 * </ul>
	 * The context is used to determine how to add a new successor relation based on the current selection.
	 * <p>
	 * Even if the tree items are not selected, the context is still required to access the
	 */
	@Getter @Accessors(fluent = true)
	public static class Context
	{
		private TreeItem<TaskBean> treeItemSelectedSuperSubTask;
		private TreeItem<TaskBean> treeItemSelectedSuccessorTask;
		private TaskServiceClient  taskServiceClient;

		public Context
		(
				@NonNull TreeItem<TaskBean> treeItemSelectedSuperSubTask,
				@NonNull TreeItem<TaskBean> treeItemSelectedSuccessorTask,
				@NonNull TaskServiceClient  taskServiceClient
		)
		{
			this.treeItemSelectedSuperSubTask  = treeItemSelectedSuperSubTask;
			this.treeItemSelectedSuccessorTask = treeItemSelectedSuccessorTask;
			this.taskServiceClient             = taskServiceClient;
		}
	}

	private final Context context;

	public ActionAdd(@NonNull Context context) { this.context = context; }

	/**
	 * Executes the action to add a new successor task.
	 * <p>
	 * If both a super/sub task and a successor task are selected, it prompts the user to choose where to add the
	 * successor task.
	 * <p>
	 * If only a super/sub task is selected, it adds the successor task to that super/sub task.
	 * <p>
	 * If neither is selected, it shows an alert dialog indicating that no valid selection was made.
	 * <p>
	 * If the selection is valid, it creates a new successor-successor relation in the database using the
	 * {@link TaskServiceClient} and updates the respective tree view.
	 */
	public void execute()
	{
		if (not(isRoot(context.treeItemSelectedSuperSubTask)) && not(isRoot(context.treeItemSelectedSuccessorTask)))
				// if a super/sub task task is selected in the tree _AND_ a successor is also selected, the user has to choose
				// if the new successor relation is targeted to the successor task or the super/sub task
				new ActionAddToSuperSubOrSuccessor(context).execute();
		else if (not(isRoot(context.treeItemSelectedSuperSubTask)))
				// if only a super/sub task is selected, the successor relation is targeted to the super/sub task
				new ActionAddToSuperSub(context).execute();
		else
				AlertDialog.showAndWait(
						"unexpected task selection",
						"Neither a successor task nor a super/sub task selected.",
						"Please select a successor task or a super/sub task to add a new successor task.",
						INFORMATION);
	}
}