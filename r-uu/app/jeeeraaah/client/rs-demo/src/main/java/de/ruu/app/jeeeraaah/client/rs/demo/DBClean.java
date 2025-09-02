package de.ruu.app.jeeeraaah.client.rs.demo;

import de.ruu.app.jeeeraaah.client.rs.TaskGroupServiceClient;
import de.ruu.app.jeeeraaah.client.rs.TaskServiceClient;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.lib.cdi.se.CDIContainer;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

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

	public void run() { cleanTaskGroups(); }

	private void cleanTaskGroups()
	{
		Set<TaskGroupEntityDTO> groups = taskGroupServiceClient.findAll();
		log.debug("task group count before clean db {}", groups.size());
		groups.forEach(g -> cleanTasksOfGroup(g));
		log.debug("task group count after  clean db {}", taskGroupServiceClient.findAll().size());
	}

	private void cleanTasksOfGroup(TaskGroupEntityDTO group)
	{
		Long taskGroupId = requireNonNull(group.id(), "task group id must not be null, persist task group to retrieve id");

		taskGroupServiceClient
				.findWithTasks(taskGroupId)
				.ifPresent
				(
						taskGroupWithTasks ->
						{
							if (taskGroupWithTasks.tasks().isPresent())
							{
								Set<TaskEntityDTO> tasks = taskGroupWithTasks.tasks().get();
								cleanTasksOfGroup(tasks);
								log.debug("deleted task group with id {} and {} tasks", taskGroupId, tasks.size());
							}
						}
				);

		taskGroupServiceClient.delete(taskGroupId);
		log.debug("deleted task group with id {}", taskGroupId);
	}

	private void cleanTasksOfGroup(Set<TaskEntityDTO> tasks)
	{
		Set<TaskEntityDTO> mainTasks = tasks.stream().filter(t -> t.superTask().isEmpty()).collect(Collectors.toSet());

		mainTasks.forEach
		(
				t ->
				{
					cleanSuperSubTaskHierarchy(t);
					log.debug("deleted task with id {}", t.id());
				}
		);
	}

	private void cleanSuperSubTaskHierarchy(TaskEntityDTO task)
	{
		// call this method recursively for all sub tasks
		task.subTasks().ifPresent(subTasks -> subTasks.forEach(subTask -> cleanSuperSubTaskHierarchy(subTask)));

		// remove all predecessor relations
		task
				.predecessors()
				.ifPresent
				(
						predecessors -> predecessors.forEach
						(
								predecessor ->
								{
									taskServiceClient.removePredecessor(task, predecessor);
									task.removePredecessor(predecessor);
								}
						)
				);

		// remove all successor relations
		task
				.successors()
				.ifPresent
						(
								successors   -> successors  .forEach
								(
										successor   ->
										{
											taskServiceClient.removeSuccessor  (task, successor  );
											task.removeSuccessor(successor);
										}
								)
						);

		taskServiceClient.delete(task.id());

		log.debug("deleted task with id {}", task.id());
	}

	public static void main(String[] args)
	{
		CDIContainer.bootstrap(DBClean.class.getClassLoader());
		DBClean command = CDI.current().select(DBClean.class).get();
		command.run();
	}
}