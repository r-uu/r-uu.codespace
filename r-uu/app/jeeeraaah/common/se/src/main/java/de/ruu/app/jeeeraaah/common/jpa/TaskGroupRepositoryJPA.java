package de.ruu.app.jeeeraaah.common.jpa;

import de.ruu.app.jeeeraaah.common.dto.TaskGroupFlat;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupLazy;
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

@Slf4j
public abstract class TaskGroupRepositoryJPA extends AbstractRepository<TaskGroupEntityJPA, Long>
{
	public Optional<TaskGroupEntityJPA> findWithTasks(@NonNull Long id)
	{
		EntityGraph<TaskGroupEntityJPA> entityGraph = entityManager().createEntityGraph(TaskGroupEntityJPA.class);
		entityGraph.addSubgraph(TaskGroupEntityJPA_.tasks.getName());

		Map<String, Object> hints = new HashMap<>();
		hints.put(GraphType.FETCH.getName(), entityGraph);

		TaskGroupEntityJPA result = entityManager().find(TaskGroupEntityJPA.class, id, hints);

		return Optional.ofNullable(result);
	}

	public Optional<TaskGroupLazy> findGroupLazy(@NonNull Long id)
	{
		Optional<TaskGroupEntityJPA> optional = findWithTasks(id);

		if (optional.isPresent())
		{
			TaskGroupEntityJPA taskGroup = optional.get();
			TaskGroupLazy      result    = new TaskGroupLazy(taskGroup.name());
			return Optional.of(result);
		}
		else return Optional.empty();
	}

	public Set<TaskGroupFlat> findAllFlat()
	{
		List<TaskGroupEntityJPA> entities = findAll();
		Set<TaskGroupFlat> 		   result   = new HashSet<>();
		entities.forEach(tg -> result.add(new TaskGroupFlat(tg)));
		return result;
	}

	public boolean deleteFromGroup(@NonNull Long idGroup, @NonNull Long idTask)
	{
		Optional<TaskGroupEntityJPA> optionalGroup = findWithTasks(idGroup);

		if (optionalGroup.isPresent())
		{
			EntityManager entityManager = entityManager();

			TaskGroupEntityJPA taskGroup = optionalGroup.get();
			TaskEntityJPA      task      = entityManager.find(TaskEntityJPA.class, idTask);

			if (task != null) // Ensure task exists before removing
			{
				if (task.subTasks().isPresent())
				{
					for (TaskEntityJPA subTask : task.subTasks().get())
					{
						task.removeSubTask(subTask);
						entityManager.merge(subTask);
					}
				}
				if (task.predecessors().isPresent())
				{
					for (TaskEntityJPA predecessor : task.predecessors().get())
					{
						task.removePredecessor(predecessor);
						entityManager.merge(predecessor);
					}
				}
				if (task.successors().isPresent())
				{
					for (TaskEntityJPA successor : task.successors().get())
					{
						task.removeSuccessor(successor);
						entityManager.merge(successor);
					}
				}

				taskGroup.removeTask(task);

				entityManager.persist(task     );
				entityManager.persist(taskGroup);

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