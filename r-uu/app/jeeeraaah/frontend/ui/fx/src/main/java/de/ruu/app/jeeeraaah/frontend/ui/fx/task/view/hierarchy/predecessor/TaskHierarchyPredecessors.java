package de.ruu.app.jeeeraaah.frontend.ui.fx.task.view.hierarchy.predecessor;

import de.ruu.app.jeeeraaah.client.fx.task.view.hierarchy.TaskHierarchyAbstract;
import jakarta.enterprise.context.Dependent;
import lombok.NonNull;

//@ApplicationScoped
@Dependent public class TaskHierarchyPredecessors extends TaskHierarchyAbstract
//		extends DefaultFXCView
//		<
//				TaskHierarchyPredecessors,
//				TaskHierarchyPredecessorsService,
//				TaskHierarchyPredecessorsController
//		>
{
	/**
	 * because {@link TaskHierarchyPredecessors} does not extend {@link de.ruu.lib.fx.comp.DefaultFXCView} this method
	 * has to be overridden and has to explicitly cast the return value of {@link TaskHierarchyAbstract#service()} to
	 * provide a {@link TaskHierarchyPredecessorsService} instance.
	 */
	@Override public @NonNull TaskHierarchyPredecessorsService service()
	{
		return (TaskHierarchyPredecessorsService) super.service();
	}
}