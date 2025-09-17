package de.ruu.app.jeeeraaah.backend.persistence.jpa;

import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTOLazy;
import de.ruu.lib.jpa.core.AbstractRepository;
import de.ruu.lib.jpa.core.GraphType;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@Slf4j
public abstract class TaskGroupRepositoryJPA extends AbstractRepository<TaskGroupJPA, Long>
{
	public Optional<TaskGroupJPA> findWithTasks(@NonNull Long id)
	{
		EntityGraph<TaskGroupJPA> entityGraph = entityManager().createEntityGraph(TaskGroupJPA.class);
		entityGraph.addSubgraph(TaskGroupJPA_.tasks.getName());

		Map<String, Object> hints = new HashMap<>();
		hints.put(GraphType.FETCH.getName(), entityGraph);

		TaskGroupJPA result = entityManager().find(TaskGroupJPA.class, id, hints);

		return Optional.ofNullable(result);
	}

	public Optional<TaskGroupLazy> findLazy(@NonNull Long id)
	{
		Optional<TaskGroupJPA> optional = findWithTasks(id);

		if (optional.isPresent())
		{
			TaskGroupJPA taskGroup = optional.get();
			TaskGroupLazy result = new TaskGroupDTOLazy(taskGroup);
			if (taskGroup.getTasks() != null)
			{
				for (TaskJPA t : taskGroup.getTasks()) result.taskIds().add(requireNonNull(t.getId()));
			}
			return Optional.of(result);
		}
		else return Optional.empty();
	}

	public Set<TaskGroupLazy> findAllLazy()
	{
		List<TaskGroupJPA> entities = findAll();
		Set<TaskGroupLazy> result   = new HashSet<>();
		for (TaskGroupJPA tgJPA : entities)
		{
			TaskGroupLazy tgLazy = new TaskGroupDTOLazy(tgJPA);
			if (tgJPA.getTasks() != null)
			{
				for (TaskJPA t : tgJPA.getTasks()) tgLazy.taskIds().add(requireNonNull(t.getId()));
			}
			result.add(tgLazy);
		}
		return result;
	}

	public boolean deleteFromGroup(@NonNull Long idGroup, @NonNull Long idTask)
	{
		Optional<TaskGroupJPA> optionalGroup = findWithTasks(idGroup);

		if (optionalGroup.isPresent())
		{
			EntityManager entityManager = entityManager();
			TaskGroupJPA  taskGroup     = optionalGroup.get();
			TaskJPA       task          = entityManager.find(TaskJPA.class, idTask); // returns managed instance or null

			if (task != null) // Ensure task exists before removing
			{
				if (task.subTasks().isPresent())
				{
					for (TaskJPA subTask : task.subTasks().get())
					{
						task.removeSubTask(subTask);
					}
				}
				if (task.predecessors().isPresent())
				{
					for (TaskJPA predecessor : task.predecessors().get())
					{
						task.removePredecessor(predecessor);
					}
				}
				if (task.successors().isPresent())
				{
					for (TaskJPA successor : task.successors().get())
					{
						task.removeSuccessor(successor);
					}
				}

				taskGroup.removeTask(task);
				// Managed entities will be flushed by the active transaction; no persist/merge required
				return true;
			}
			else
			{
				log.warn("Task with id {} not found in group with id {}", idTask, idGroup);
				return false;
			}
		}
		else
		{
			log.warn("Task group with id {} not found for deletion of task with id {}", idGroup, idTask);
			return false;
		}
	}
}