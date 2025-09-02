package de.ruu.app.jeeeraaah.client.fx.task;

import de.ruu.app.jeeeraaah.client.fx.taskgroup.TaskGroupManagementService;
import de.ruu.lib.cdi.se.EventDispatcher;
import de.ruu.lib.util.AbstractEvent;
import jakarta.enterprise.context.ApplicationScoped;

/** Event that can be thrown to indicate that a {@link TaskGroupManagementService} has requested to be stopped */
public class TaskManagementDisposeRequestEvent extends AbstractEvent<TaskManagementService, Object>
{
	public TaskManagementDisposeRequestEvent(final TaskManagementService source) { super(source); }

	@ApplicationScoped public static class TaskManagerDisposeRequestEventDispatcher
			extends EventDispatcher<TaskManagementDisposeRequestEvent> { }
}