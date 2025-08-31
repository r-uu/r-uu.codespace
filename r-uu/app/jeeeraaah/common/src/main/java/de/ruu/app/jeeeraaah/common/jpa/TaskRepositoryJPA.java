package de.ruu.app.jeeeraaah.common.jpa;

import de.ruu.app.jeeeraaah.common.RemoveNeighboursFromTaskConfig;
import de.ruu.app.jeeeraaah.common.TaskRelationException;
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

//		Map<String, Object> hints = new HashMap<>();
//		hints.put(GraphType.FETCH.getName(), entityGraph);

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

	public void addSubTask(@NonNull Long taskId, @NonNull Long subTaskId) throws NotFoundException
	{
		TaskEntityJPA persistedTask = entityManager().find(TaskEntityJPA.class, taskId);
		if (isNull(persistedTask)) throw new NotFoundException("super task not found");

		TaskEntityJPA persistedSubTask = entityManager().find(TaskEntityJPA.class, subTaskId);
		if (isNull(persistedSubTask)) throw new NotFoundException("sub task not found");

		if (not(persistedTask.addSubTask(persistedSubTask)))
				throw new RuntimeException("failure adding sub task with id " + subTaskId + " to task with id " + taskId);

		entityManager().merge(persistedTask);
		entityManager().merge(persistedSubTask);
	}

	public void addPredecessor(@NonNull Long taskId, @NonNull Long predecessorId)
			throws NotFoundException, TaskRelationException
	{
		TaskEntityJPA persistedTask = entityManager().find(TaskEntityJPA.class, taskId);
		if (isNull(persistedTask)) throw new NotFoundException("super task not found");

		TaskEntityJPA persistedPreTask = entityManager().find(TaskEntityJPA.class, predecessorId);
		if (isNull(persistedPreTask)) throw new NotFoundException("predecessor task not found");

		if (not(persistedTask.addPredecessor(persistedPreTask)))
				throw new TaskRelationException("failure adding predecessor with id " + predecessorId + " to task with id " + taskId);

		entityManager().merge(persistedTask);
		entityManager().merge(persistedPreTask);
	}

	public void addSuccessor(@NonNull Long taskId, @NonNull Long successorId)
			throws NotFoundException, TaskRelationException
	{
		TaskEntityJPA persistedTask = entityManager().find(TaskEntityJPA.class, taskId);
		if (isNull(persistedTask)) throw new NotFoundException("super task not found");

		TaskEntityJPA persistedSucTask = entityManager().find(TaskEntityJPA.class, successorId);
		if (isNull(persistedSucTask)) throw new NotFoundException("successor task not found");

		if (not(persistedTask.addSuccessor(persistedSucTask)))
				throw new TaskRelationException("failure adding successor with id " + successorId + " to task with id " + taskId);

		entityManager().merge(persistedTask);
		entityManager().merge(persistedSucTask);
	}

	public void removeSubTask(@NonNull Long taskId, @NonNull Long subTaskId)
			throws NotFoundException, TaskRelationException
	{
		Optional<TaskEntityJPA> optional = findWithRelated(taskId);
		if (not(optional.isPresent()))  throw new NotFoundException("super task not found");
		TaskEntityJPA persistedTask = optional.get();

		optional = findWithRelated(subTaskId);
		if (not(optional.isPresent()))  throw new NotFoundException("sub task not found");
		TaskEntityJPA persistedSubTask = optional.get();

		if (not(persistedTask.removeSubTask(persistedSubTask)))
				throw new TaskRelationException("failure removing sub task with id " + subTaskId + " from task with id " + taskId);

		entityManager().merge(persistedTask);
		entityManager().merge(persistedSubTask);
	}

	public void removePredecessor(@NonNull Long taskId, @NonNull Long predecessorId)
			throws NotFoundException, TaskRelationException
	{
		Optional<TaskEntityJPA> optional = findWithRelated(taskId);
		if (not(optional.isPresent()))  throw new NotFoundException("successor not found");
		TaskEntityJPA persistedTask = optional.get();

		optional = findWithRelated(predecessorId);
		if (not(optional.isPresent()))  throw new NotFoundException("predecessor not found");
		TaskEntityJPA persistedPredecessor = optional.get();

		if (not(persistedTask.removePredecessor(persistedPredecessor)))
				throw new TaskRelationException("failure removing predecessor with id " + predecessorId + " from task with id " + taskId);

		entityManager().merge(persistedTask);
		entityManager().merge(persistedPredecessor);
	}

	public void removeSuccessor(@NonNull Long taskId, @NonNull Long successorId)
			throws NotFoundException, TaskRelationException
	{
		Optional<TaskEntityJPA> optional = findWithRelated(taskId);
		if (not(optional.isPresent()))  throw new NotFoundException("predecessor not found");
		TaskEntityJPA persistedTask = optional.get();

		optional = findWithRelated(successorId);
		if (not(optional.isPresent()))  throw new NotFoundException("successor not found");
		TaskEntityJPA persistedSuccessor = optional.get();

		if (not(persistedTask.removeSuccessor(persistedSuccessor)))
				throw new TaskRelationException("failure removing successor with id " + successorId + " from task with id " + taskId);

		entityManager().merge(persistedTask);
		entityManager().merge(persistedSuccessor);
	}

	public void removeNeighboursFromTask(@NonNull RemoveNeighboursFromTaskConfig config)
			throws NotFoundException, TaskRelationException
	{
		Optional<TaskEntityJPA> optional      = findWithRelated(config.idTask());
		if (not(optional.isPresent())) throw new NotFoundException("task not found, id: " + config.idTask());
		TaskEntityJPA           persistedTask = optional.get();

		if (config.removeFromSuperTask())
		{
			Optional<TaskEntityJPA> optionalSuperTask = persistedTask.superTask();
			if (not(optionalSuperTask.isPresent())) throw new NotFoundException("super task not found");
			TaskEntityJPA persistedSuperTask = optionalSuperTask.get();

			if (not(persistedSuperTask.removeSubTask(persistedTask)))
				throw new TaskRelationException(
						  "failure removing sub task with id " + config.idTask()
						+ " from super task with id " + persistedSuperTask.getId());
			entityManager().merge(persistedSuperTask);
		}

		for (Long idSubTask : config.removeFromSubTasks())
		{
			Optional<TaskEntityJPA> optionalSubTask = findWithRelated(idSubTask);
			if (not(optionalSubTask.isPresent())) throw new NotFoundException("sub task with id " + idSubTask + " not found");
			TaskEntityJPA persistedSubTask = optionalSubTask.get();

			if (not(persistedTask.removeSubTask(persistedSubTask)))
					throw new TaskRelationException(
							  "failure removing sub task with id " + idSubTask
							+ " from task with id " + config.idTask());
			entityManager().merge(persistedSubTask);
		}

		for (Long idPredecessor : config.removeFromPredecessors())
		{
			Optional<TaskEntityJPA> optionalPredecessor = findWithRelated(idPredecessor);
			if (not(optionalPredecessor.isPresent())) throw new NotFoundException("predecessor with id " + idPredecessor + " not found");
			TaskEntityJPA persistedPredecessor = optionalPredecessor.get();

			if (not(persistedTask.removePredecessor(persistedPredecessor)))
					throw new RuntimeException(
							  "failure removing predecessor with id " + idPredecessor
							+ " from task with id " + config.idTask());
			entityManager().merge(persistedPredecessor);
		}

		for (Long idSuccessor : config.removeFromSuccessors())
		{
			Optional<TaskEntityJPA> optionalSuccessor = findWithRelated(idSuccessor);
			if (not(optionalSuccessor.isPresent())) throw new NotFoundException("successor with id " + idSuccessor + " not found");
			TaskEntityJPA persistedSuccessor = optionalSuccessor.get();

			if (not(persistedTask.removePredecessor(persistedSuccessor)))
				throw new TaskRelationException(
						  "failure removing predecessor with id " + idSuccessor
						+ " from task with id " + config.idTask());
			entityManager().merge(persistedSuccessor);
		}
	}
}