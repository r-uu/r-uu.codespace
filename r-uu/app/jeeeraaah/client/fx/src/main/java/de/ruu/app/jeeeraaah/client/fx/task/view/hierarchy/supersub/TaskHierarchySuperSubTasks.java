package de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.supersub;

import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.TaskHierarchyAbstract;
import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.predecessor.TaskHierarchyPredecessors;
import jakarta.enterprise.context.Dependent;
import lombok.NonNull;

//@ApplicationScoped
@Dependent public class TaskHierarchySuperSubTasks extends TaskHierarchyAbstract
{
	/**
	 * because {@link TaskHierarchyPredecessors} does not extend {@link de.ruu.lib.fx.comp.DefaultFXCView} this method
	 * has to be overridden and has to explicitly cast the return value of {@link TaskHierarchyAbstract#service()} to
	 * provide a {@link TaskHierarchySuperSubTasksService} instance.
	 */
	@Override public @NonNull TaskHierarchySuperSubTasksService service()
	{
		return (TaskHierarchySuperSubTasksService) super.service();
	}
}