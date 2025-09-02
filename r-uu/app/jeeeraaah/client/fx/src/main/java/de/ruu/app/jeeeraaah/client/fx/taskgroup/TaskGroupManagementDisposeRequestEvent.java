package de.ruu.app.jeeeraaah.client.fx.taskgroup;

import de.ruu.lib.cdi.se.EventDispatcher;
import de.ruu.lib.util.AbstractEvent;
import jakarta.enterprise.context.ApplicationScoped;

/** Event that can be thrown to indicate that a {@link TaskGroupManagementService} has requested to be stopped */
public class TaskGroupManagementDisposeRequestEvent extends AbstractEvent<TaskGroupManagementService, Object>
{
	@ApplicationScoped public static class TaskGroupManagerDisposeRequestEventDispatcher extends EventDispatcher<TaskGroupManagementDisposeRequestEvent> { }
	public TaskGroupManagementDisposeRequestEvent(final TaskGroupManagementService source) { super(source); }
	/** programmatically specify command line vm option {@code --add-reads de.ruu.app.jeeeraah.client.fx=ALL-UNNAMED} */
	public static void addReadsUnnamedModule()
	{
		TaskGroupManagementDisposeRequestEvent
				.class
				.getModule()
				.addReads(TaskGroupManagementDisposeRequestEvent.class.getClassLoader().getUnnamedModule());
	}
}