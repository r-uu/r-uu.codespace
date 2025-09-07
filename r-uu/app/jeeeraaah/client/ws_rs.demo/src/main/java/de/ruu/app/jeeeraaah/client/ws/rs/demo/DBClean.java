package de.ruu.app.jeeeraaah.client.ws.rs.demo;

import de.ruu.app.jeeeraaah.client.ws.rs.TaskGroupServiceClient;
import de.ruu.app.jeeeraaah.client.ws.rs.TaskServiceClient;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.lib.cdi.se.CDIContainer;
import de.ruu.lib.ws.rs.NonTechnicalException;
import de.ruu.lib.ws.rs.TechnicalException;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static de.ruu.lib.util.BooleanFunctions.not;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

@Slf4j
public class DBClean
{
	@Inject private TaskGroupServiceClient taskGroupServiceClient;
	@Inject private TaskServiceClient      taskServiceClient;

	@PostConstruct private void postConstruct()
	{
		log.debug ("initialised rs-clients successfully: {}", not(isNull(taskGroupServiceClient)) && not(isNull(taskServiceClient)));
	}

	public void run() throws NonTechnicalException, TechnicalException { cleanTaskGroups(); }

	private void cleanTaskGroups() throws NonTechnicalException, TechnicalException
	{
		Set<TaskGroupBean> groups = taskGroupServiceClient.findAll();

		log.debug("task group count before clean db {}", groups.size());
		for (TaskGroupBean group : groups) { cleanTasksOfGroup(group); }
		log.debug("task group count after  clean db {}", taskGroupServiceClient.findAll().size());
	}

	private void cleanTasksOfGroup(TaskGroupBean group) throws NonTechnicalException, TechnicalException
	{
		Long taskGroupId = requireNonNull(group.id(), "task group id must not be null, persist task group to retrieve id");

		Optional<TaskGroupBean> optionalTaskGroup = taskGroupServiceClient.findWithTasks(taskGroupId);

		if (optionalTaskGroup.isPresent())
		{
			TaskGroupBean taskGroup = optionalTaskGroup.get();
			cleanTasksOfGroup(taskGroup);
			int size = 0;
			taskGroup.tasks().ifPresent(ts -> ts.size());
			log.debug("deleted task group with id {} and {} tasks", taskGroupId, size);
		}

		taskGroupServiceClient.delete(taskGroupId);
		log.debug("deleted task group with id {}", taskGroupId);
	}

	private void cleanTasksOfGroup(Set<TaskBean> tasks) throws NonTechnicalException, TechnicalException
	{
		Set<TaskBean> mainTasks = tasks.stream().filter(t -> t.superTask().isEmpty()).collect(Collectors.toSet());

		for (TaskBean mainTask : mainTasks)
		{
			cleanSuperSubTaskHierarchy(mainTask);
			log.debug("deleted task with id {}", mainTask.id());
		}
	}

	private void cleanSuperSubTaskHierarchy(TaskBean task) throws NonTechnicalException, TechnicalException
	{
		// call this method recursively for all sub tasks
		if (task.subTasks().isPresent())
				for (TaskBean subTask : task.subTasks().get()) { cleanSuperSubTaskHierarchy(subTask); }

		// remove all predecessor relations
		if (task.predecessors().isPresent())
		{
			for (TaskBean predecessor : task.predecessors().get())
			{
					taskServiceClient.removePredecessor(task, predecessor);
					task.removePredecessor(predecessor);
			}
		}

		// remove all successor relations
		if (task.successors().isPresent())
		{
			for (TaskBean successor : task.successors().get())
			{
				taskServiceClient.removeSuccessor(task, successor);
				task.removeSuccessor(successor);
			}
		}

		taskServiceClient.delete(task.id());

		log.debug("deleted task with id {}", task.id());
	}

	public static void main(String[] args) throws NonTechnicalException, TechnicalException
	{
		CDIContainer.bootstrap(DBClean.class.getClassLoader());
		DBClean command = CDI.current().select(DBClean.class).get();
		command.run();
	}
}