package de.ruu.app.jeeeraaah.common.jpa;

import de.ruu.app.jeeeraaah.common.RemoveNeighboursFromTaskConfig;
import de.ruu.app.jeeeraaah.common.TaskService;
import de.ruu.app.jeeeraaah.common.TaskService.TaskRelationException;
import de.ruu.app.jeeeraaah.common.dto.TaskLazy;
import de.ruu.lib.jpa.core.AbstractRepository;
import de.ruu.lib.jpa.core.GraphType;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.ws.rs.NotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static de.ruu.app.jeeeraaah.common.TaskService.TaskRelationException;
import static de.ruu.lib.util.BooleanFunctions.not;
import static java.util.Objects.isNull;

@Slf4j
public abstract class TaskRepositoryJPA extends AbstractRepository<TaskEntityJPA, Long>
{
	public Optional<TaskEntityJPA> findWithRelated(@NonNull Long id)
	{
		EntityGraph<TaskEntityJPA> entityGraph = entityManager().createEntityGraph(TaskEntityJPA.class);
		entityGraph.addSubgraph(TaskEntityJPA_.subTasks    .getName());
		entityGraph.addSubgraph(TaskEntityJPA_.predecessors.getName());
		entityGraph.addSubgraph(TaskEntityJPA_.successors  .getName());

		Map<String, Object> hints = new HashMap<>();
		hints.put(GraphType.FETCH.getName(), entityGraph);

		TaskEntityJPA result = entityManager().find(TaskEntityJPA.class, id, hints);

		return Optional.ofNullable(result);
	}

	public @NonNull Set<TaskLazy> findWithRelated(@NonNull Set<Long> ids)
	{
		CriteriaBuilder              criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<TaskEntityJPA> criteriaQuery   = criteriaBuilder.createQuery(TaskEntityJPA.class);
		Root<TaskEntityJPA>          root            = criteriaQuery  .from       (TaskEntityJPA.class);

		criteriaQuery.select(root).where(root.get(TaskEntityJPA_.id).in(ids));

		EntityGraph<TaskEntityJPA> entityGraph = entityManager().createEntityGraph(TaskEntityJPA.class);
		entityGraph.addSubgraph(TaskEntityJPA_.subTasks    .getName());
		entityGraph.addSubgraph(TaskEntityJPA_.predecessors.getName());
		entityGraph.addSubgraph(TaskEntityJPA_.successors  .getName());

		Set<TaskEntityJPA> taskEntities =
				new HashSet<>(
						entityManager()
								.createQuery(criteriaQuery)
								.setHint(GraphType.FETCH.getName(), entityGraph)
								.getResultList());

		Set<TaskLazy> result = new HashSet<>();

		taskEntities.forEach(t -> result.add(t.toLazy()));

		return result;
	}

	public void addSubTask(@NonNull Long taskId, @NonNull Long subTaskId) throws TaskRelationException
	{
		TaskEntityJPA persistedTask = entityManager().find(TaskEntityJPA.class, taskId);
		if (isNull(persistedTask)) throw new TaskRelationException("super task not found, id: " + taskId);

		TaskEntityJPA persistedSubTask = entityManager().find(TaskEntityJPA.class, subTaskId);
		if (isNull(persistedSubTask)) throw new TaskRelationException("sub task not found, id: " + subTaskId);

		if (not(persistedTask.addSubTask(persistedSubTask)))
				throw new RuntimeException("failure adding sub task with id " + subTaskId + " to task with id " + taskId);

		entityManager().merge(persistedTask);
		entityManager().merge(persistedSubTask);
	}

	public void addPredecessor(@NonNull Long taskId, @NonNull Long predecessorId) throws TaskRelationException
	{
		TaskEntityJPA persistedTask = entityManager().find(TaskEntityJPA.class, taskId);
		if (isNull(persistedTask)) throw new TaskRelationException("successor task not found, id: " + taskId);

		TaskEntityJPA persistedPreTask = entityManager().find(TaskEntityJPA.class, predecessorId);
		if (isNull(persistedPreTask)) throw new TaskRelationException("predecessor task not found, id: " + predecessorId);

		if (not(persistedTask.addPredecessor(persistedPreTask)))
				throw new TaskRelationException(
						"failure adding predecessor with id " + predecessorId + " to task with id " + taskId);

		entityManager().merge(persistedTask);
		entityManager().merge(persistedPreTask);
	}

	public void addSuccessor(@NonNull Long taskId, @NonNull Long successorId) throws TaskRelationException
	{
		TaskEntityJPA persistedTask = entityManager().find(TaskEntityJPA.class, taskId);
		if (isNull(persistedTask)) throw new TaskRelationException("predecessor task not found, id: " + taskId);

		TaskEntityJPA persistedSucTask = entityManager().find(TaskEntityJPA.class, successorId);
		if (isNull(persistedSucTask)) throw new TaskRelationException("successor task not found, id: " + successorId);

		if (not(persistedTask.addSuccessor(persistedSucTask)))
				throw new TaskRelationException(
						"failure adding successor with id " + successorId + " to task with id: " + taskId);

		entityManager().merge(persistedTask);
		entityManager().merge(persistedSucTask);
	}

	public void removeSubTask(@NonNull Long taskId, @NonNull Long subTaskId) throws TaskRelationException
	{
		Optional<TaskEntityJPA> optional = findWithRelated(taskId);
		if (not(optional.isPresent()))  throw new TaskRelationException("super task not found, id: " + taskId);
		TaskEntityJPA persistedTask = optional.get();

		optional = findWithRelated(subTaskId);
		if (not(optional.isPresent()))  throw new TaskRelationException("sub task not found, id: " + subTaskId);
		TaskEntityJPA persistedSubTask = optional.get();

		if (not(persistedTask.removeSubTask(persistedSubTask)))
				throw new TaskRelationException(
						"failure removing sub task with id [" + subTaskId + "] from task with id [" + taskId + "]");

		entityManager().merge(persistedTask);
		entityManager().merge(persistedSubTask);
	}

	public void removePredecessor(@NonNull Long taskId, @NonNull Long predecessorId) throws TaskRelationException
	{
		Optional<TaskEntityJPA> optional = findWithRelated(taskId);
		if (not(optional.isPresent()))  throw new TaskRelationException("successor not found, id: " + taskId);
		TaskEntityJPA persistedTask = optional.get();

		optional = findWithRelated(predecessorId);
		if (not(optional.isPresent()))  throw new TaskRelationException("predecessor not found, id:" + predecessorId);
		TaskEntityJPA persistedPredecessor = optional.get();

		if (not(persistedTask.removePredecessor(persistedPredecessor)))
				throw new TaskRelationException(
						"failure removing predecessor with id [" + predecessorId + "] from task with id [" + taskId + "]");

		entityManager().merge(persistedTask);
		entityManager().merge(persistedPredecessor);
	}

	public void removeSuccessor(@NonNull Long taskId, @NonNull Long successorId) throws TaskRelationException
	{
		Optional<TaskEntityJPA> optional = findWithRelated(taskId);
		if (not(optional.isPresent()))  throw new TaskRelationException("predecessor not found: id: " + taskId);
		TaskEntityJPA persistedTask = optional.get();

		optional = findWithRelated(successorId);
		if (not(optional.isPresent()))  throw new TaskRelationException("successor not found, id: " + successorId);
		TaskEntityJPA persistedSuccessor = optional.get();

		if (not(persistedTask.removeSuccessor(persistedSuccessor)))
				throw new TaskRelationException(
						"failure removing successor with id [" + successorId + "] from task with id [" + taskId + "]");

		entityManager().merge(persistedTask);
		entityManager().merge(persistedSuccessor);
	}

	public void removeNeighboursFromTask(@NonNull RemoveNeighboursFromTaskConfig config) throws TaskRelationException
	{
		Optional<TaskEntityJPA> optional      = findWithRelated(config.idTask());
		if (not(optional.isPresent())) throw new TaskRelationException("task not found, id: " + config.idTask());
		TaskEntityJPA           persistedTask = optional.get();

		if (config.removeFromSuperTask())
		{
			Optional<TaskEntityJPA> optionalSuperTask = persistedTask.superTask();
			if (not(optionalSuperTask.isPresent()))
					throw new TaskRelationException("super task not found, id: " + config.idTask());
			TaskEntityJPA persistedSuperTask = optionalSuperTask.get();

			if (not(persistedSuperTask.removeSubTask(persistedTask)))
				throw new TaskRelationException(
						  "failure removing sub task with id [" + config.idTask()
						+ "] from super task with id [" + persistedSuperTask.getId() + "]");
			entityManager().merge(persistedSuperTask);
		}

		for (Long idSubTask : config.removeFromSubTasks())
		{
			Optional<TaskEntityJPA> optionalSubTask = findWithRelated(idSubTask);
			if (not(optionalSubTask.isPresent())) throw new TaskRelationException("sub task not found, id: " + idSubTask);
			TaskEntityJPA persistedSubTask = optionalSubTask.get();

			if (not(persistedTask.removeSubTask(persistedSubTask)))
					throw new TaskRelationException(
							  "failure removing sub task with id [" + idSubTask
							+ "] from task with id [" + config.idTask() + "]");
			entityManager().merge(persistedSubTask);
		}

		for (Long idPredecessor : config.removeFromPredecessors())
		{
			Optional<TaskEntityJPA> optionalPredecessor = findWithRelated(idPredecessor);
			if (not(optionalPredecessor.isPresent()))
					throw new TaskRelationException("predecessor not found, id: " + idPredecessor);
			TaskEntityJPA persistedPredecessor = optionalPredecessor.get();

			if (not(persistedTask.removePredecessor(persistedPredecessor)))
					throw new TaskRelationException(
							  "failure removing predecessor with id [" + idPredecessor
							+ "] from task with id [" + config.idTask() + "]");
			entityManager().merge(persistedPredecessor);
		}

		for (Long idSuccessor : config.removeFromSuccessors())
		{
			Optional<TaskEntityJPA> optionalSuccessor = findWithRelated(idSuccessor);
			if (not(optionalSuccessor.isPresent())) throw new TaskRelationException("successor not found, id: " + idSuccessor);
			TaskEntityJPA persistedSuccessor = optionalSuccessor.get();

			if (not(persistedTask.removePredecessor(persistedSuccessor)))
				throw new TaskRelationException(
						  "failure removing predecessor with id [" + idSuccessor
						+ "] from task with id [" + config.idTask() + "]");
			entityManager().merge(persistedSuccessor);
		}
	}
}