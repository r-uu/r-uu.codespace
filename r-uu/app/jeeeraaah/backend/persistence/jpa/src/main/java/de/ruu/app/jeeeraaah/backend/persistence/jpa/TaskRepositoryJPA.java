package de.ruu.app.jeeeraaah.backend.persistence.jpa;

import de.ruu.app.jeeeraaah.common.api.domain.RemoveNeighboursFromTaskConfig;
import de.ruu.app.jeeeraaah.common.api.domain.TaskService.TaskRelationException;
import de.ruu.lib.jpa.core.AbstractRepository;
import de.ruu.lib.jpa.core.GraphType;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static de.ruu.lib.util.BooleanFunctions.not;

@Slf4j
public abstract class TaskRepositoryJPA extends AbstractRepository<TaskJPA, Long>
{
	public Optional<TaskJPA> findWithRelated(@NonNull Long id)
	{
		EntityGraph<TaskJPA> entityGraph = entityManager().createEntityGraph(TaskJPA.class);

		entityGraph.addSubgraph(TaskEntityJPA_.subTasks    .getName());
		entityGraph.addSubgraph(TaskEntityJPA_.predecessors.getName());
		entityGraph.addSubgraph(TaskEntityJPA_.successors  .getName());

		Map<String, Object> hints = new HashMap<>();
		hints.put(GraphType.FETCH.getName(), entityGraph);

		TaskJPA result = entityManager().find(TaskJPA.class, id, hints);

		return Optional.ofNullable(result);
	}

	public @NonNull Set<TaskJPA> findWithRelated(@NonNull Set<Long> ids)
	{
		CriteriaBuilder        criteriaBuilder = entityManager().getCriteriaBuilder();
		CriteriaQuery<TaskJPA> criteriaQuery   = criteriaBuilder.createQuery(TaskJPA.class);
		Root<TaskJPA>          root            = criteriaQuery  .from       (TaskJPA.class);

		criteriaQuery.select(root).where(root.get("id").in(ids));

		EntityGraph<TaskJPA> entityGraph = entityManager().createEntityGraph(TaskJPA.class);
		entityGraph.addSubgraph("subTasks");
		entityGraph.addSubgraph("predecessors");
		entityGraph.addSubgraph("successors");

		Set<TaskJPA> taskEntities =
				new HashSet<>(
						entityManager()
								.createQuery(criteriaQuery)
								.setHint(GraphType.FETCH.getName(), entityGraph)
								.getResultList());

		return taskEntities;
	}

	public void addSubTask(@NonNull Long taskId, @NonNull Long subTaskId) throws TaskRelationException
	{
		TaskJPA persistedTask = findOrThrow(taskId);
		TaskJPA persistedSubTask = findOrThrow(subTaskId);

		if (not(persistedTask.addSubTask(persistedSubTask)))
				throw new RuntimeException("failure adding sub task with id " + subTaskId + " to task with id " + taskId);
	}

	public void addPredecessor(@NonNull Long taskId, @NonNull Long predecessorId) throws TaskRelationException
	{
		TaskJPA persistedTask = findOrThrow(taskId);
		TaskJPA persistedPreTask = findOrThrow(predecessorId);

		if (not(persistedTask.addPredecessor(persistedPreTask)))
				throw new TaskRelationException(
						"failure adding predecessor with id " + predecessorId + " to task with id " + taskId);
	}

	public void addSuccessor(@NonNull Long taskId, @NonNull Long successorId) throws TaskRelationException
	{
		TaskJPA persistedTask = findOrThrow(taskId);
		TaskJPA persistedSucTask = findOrThrow(successorId);

		if (not(persistedTask.addSuccessor(persistedSucTask)))
				throw new TaskRelationException(
						"failure adding successor with id " + successorId + " to task with id: " + taskId);
	}

	public void removeSubTask(@NonNull Long taskId, @NonNull Long subTaskId) throws TaskRelationException
	{
		Optional<TaskJPA> optional = findWithRelated(taskId);
		if (not(optional.isPresent()))  throw new TaskRelationException("super task not found, id: " + taskId);
		TaskJPA persistedTask = optional.get();

		optional = findWithRelated(subTaskId);
		if (not(optional.isPresent()))  throw new TaskRelationException("sub task not found, id: " + subTaskId);
		TaskJPA persistedSubTask = optional.get();

		if (not(persistedTask.removeSubTask(persistedSubTask)))
				throw new TaskRelationException(
						"failure removing sub task with id [" + subTaskId + "] from task with id [" + taskId + "]");
	}

	public void removePredecessor(@NonNull Long taskId, @NonNull Long predecessorId) throws TaskRelationException
	{
		Optional<TaskJPA> optional = findWithRelated(taskId);
		if (not(optional.isPresent()))  throw new TaskRelationException("successor not found, id: " + taskId);
		TaskJPA persistedTask = optional.get();

		optional = findWithRelated(predecessorId);
		if (not(optional.isPresent()))  throw new TaskRelationException("predecessor not found, id:" + predecessorId);
		TaskJPA persistedPredecessor = optional.get();

		if (not(persistedTask.removePredecessor(persistedPredecessor)))
				throw new TaskRelationException(
						"failure removing predecessor with id [" + predecessorId + "] from task with id [" + taskId + "]");

		// managed entities; no merge() required
	}

	public void removeSuccessor(@NonNull Long taskId, @NonNull Long successorId) throws TaskRelationException
	{
		Optional<TaskJPA> optional = findWithRelated(taskId);
		if (not(optional.isPresent()))  throw new TaskRelationException("predecessor not found: id: " + taskId);
		TaskJPA persistedTask = optional.get();

		optional = findWithRelated(successorId);
		if (not(optional.isPresent()))  throw new TaskRelationException("successor not found, id: " + successorId);
		TaskJPA persistedSuccessor = optional.get();

		if (not(persistedTask.removeSuccessor(persistedSuccessor)))
				throw new TaskRelationException(
						"failure removing successor with id [" + successorId + "] from task with id [" + taskId + "]");

		// managed entities; no merge() required
	}

	public void removeNeighboursFromTask(@NonNull RemoveNeighboursFromTaskConfig config) throws TaskRelationException
	{
		Optional<TaskJPA> optional      = findWithRelated(config.idTask());
		if (not(optional.isPresent())) throw new TaskRelationException("task not found, id: " + config.idTask());
		TaskJPA persistedTask = optional.get();

		if (config.removeFromSuperTask())
		{
			Optional<TaskJPA> optionalSuperTask = persistedTask.superTask();
			if (not(optionalSuperTask.isPresent()))
					throw new TaskRelationException("super task not found, id: " + config.idTask());
			TaskJPA persistedSuperTask = optionalSuperTask.get();

			if (not(persistedSuperTask.removeSubTask(persistedTask)))
				throw new TaskRelationException(
						  "failure removing sub task with id [" + config.idTask()
						+ "] from super task with id [" + persistedSuperTask.getId() + "]");
			entityManager().merge(persistedSuperTask);
		}

		for (Long idSubTask : config.removeFromSubTasks())
		{
			Optional<TaskJPA> optionalSubTask = findWithRelated(idSubTask);
			if (not(optionalSubTask.isPresent())) throw new TaskRelationException("sub task not found, id: " + idSubTask);
			TaskJPA persistedSubTask = optionalSubTask.get();

			if (not(persistedTask.removeSubTask(persistedSubTask)))
					throw new TaskRelationException(
							  "failure removing sub task with id [" + idSubTask
							+ "] from task with id [" + config.idTask() + "]");
			entityManager().merge(persistedSubTask);
		}

		for (Long idPredecessor : config.removeFromPredecessors())
		{
			Optional<TaskJPA> optionalPredecessor = findWithRelated(idPredecessor);
			if (not(optionalPredecessor.isPresent()))
					throw new TaskRelationException("predecessor not found, id: " + idPredecessor);
			TaskJPA persistedPredecessor = optionalPredecessor.get();

			if (not(persistedTask.removePredecessor(persistedPredecessor)))
					throw new TaskRelationException(
							  "failure removing predecessor with id [" + idPredecessor
							+ "] from task with id [" + config.idTask() + "]");
			entityManager().merge(persistedPredecessor);
		}

		for (Long idSuccessor : config.removeFromSuccessors())
		{
			Optional<TaskJPA> optionalSuccessor = findWithRelated(idSuccessor);
			if (not(optionalSuccessor.isPresent())) throw new TaskRelationException("successor not found, id: " + idSuccessor);
			TaskJPA persistedSuccessor = optionalSuccessor.get();

			if (not(persistedTask.removePredecessor(persistedSuccessor)))
				throw new TaskRelationException(
						  "failure removing predecessor with id [" + idSuccessor
						+ "] from task with id [" + config.idTask() + "]");
			entityManager().merge(persistedSuccessor);
		}
	}
}