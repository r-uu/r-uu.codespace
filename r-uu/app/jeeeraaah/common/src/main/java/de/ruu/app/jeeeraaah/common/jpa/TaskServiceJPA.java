package de.ruu.app.jeeeraaah.common.jpa;

import de.ruu.app.jeeeraaah.common.RemoveNeighboursFromTaskConfig;
import de.ruu.app.jeeeraaah.common.TaskService;
import de.ruu.app.jeeeraaah.common.TaskServiceLazy;
import de.ruu.app.jeeeraaah.common.dto.TaskLazy;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.NotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static de.ruu.lib.util.BooleanFunctions.not;
import static java.util.Objects.isNull;

/** TODO: rename and move this type (no JPA suffix) */
@Slf4j
public abstract class TaskServiceJPA
		implements TaskService<TaskEntityJPA>, TaskServiceLazy
{
	protected abstract TaskRepositoryJPA repository();

	@PostConstruct private void postConstruct() { log.debug("repository available: {}", not(isNull(repository()))); }

	@Override public @NonNull TaskEntityJPA           create(@NonNull TaskEntityJPA entity) { return repository().save(entity); }
	@Override public @NonNull Optional<TaskEntityJPA> read  (@NonNull Long          id    ) { return repository().find(id    ); }
	@Override public @NonNull TaskEntityJPA           update(@NonNull TaskEntityJPA entity) { return repository().save(entity); }
	@Override public          void                    delete(@NonNull Long id             )
	{
		                                    repository().delete(id);
	}

	@Override public @NonNull Set<TaskEntityJPA> findAll()
	{
		return new HashSet<>(repository().findAll());
	}

	@Override public Optional<TaskEntityJPA> findWithRelated(@NonNull Long id)
	{
		return repository().findWithRelated(id);
	}

	@Override public Set<TaskLazy> findTasksLazy(@NonNull Set<Long> ids) { return repository().findWithRelated(ids); }

	@Override public void addSubTask(@NonNull TaskEntityJPA task, @NonNull TaskEntityJPA subTask) throws NotFoundException
	{
		if (isNull(task   .id())) throw new IllegalArgumentException(    "task id must not be null");
		if (isNull(subTask.id())) throw new IllegalArgumentException("sub task id must not be null");
		repository().addSubTask(task.id(), subTask.id());
	}

	@Override public void addPredecessor(@NonNull TaskEntityJPA task, @NonNull TaskEntityJPA predecessor)
	{
		if (isNull(task       .id())) throw new IllegalArgumentException(       "task id must not be null");
		if (isNull(predecessor.id())) throw new IllegalArgumentException("predecessor id must not be null");
		repository().addPredecessor(task.id(), predecessor.id());
	}

	@Override public void addSuccessor(@NonNull TaskEntityJPA task, @NonNull TaskEntityJPA successor)
	{
		if (isNull(task     .id())) throw new IllegalArgumentException(     "task id must not be null");
		if (isNull(successor.id())) throw new IllegalArgumentException("successor id must not be null");
		repository().addSuccessor(task.id(), successor.id());
	}

	@Override public void removeSuperSubTask(@NonNull TaskEntityJPA task, @NonNull TaskEntityJPA subTask)
	{
		if (isNull(task   .id())) throw new IllegalArgumentException(    "task id must not be null");
		if (isNull(subTask.id())) throw new IllegalArgumentException("sub task id must not be null");
		repository().removeSubTask(task.id(), subTask.id());
	}

	@Override public void removePredecessor(@NonNull TaskEntityJPA task, @NonNull TaskEntityJPA predecessor)
	{
		if (isNull(task       .id())) throw new IllegalArgumentException(       "task id must not be null");
		if (isNull(predecessor.id())) throw new IllegalArgumentException("predecessor id must not be null");
		repository().removePredecessor(task.id(), predecessor.id());
	}

	@Override public void removeSuccessor(@NonNull TaskEntityJPA task, @NonNull TaskEntityJPA successor)
	{
		if (isNull(task     .id())) throw new IllegalArgumentException(     "task id must not be null");
		if (isNull(successor.id())) throw new IllegalArgumentException("successor id must not be null");
		repository().removeSuccessor(task.id(), successor.id());
	}

	@Override public boolean removeNeighboursFromTask(@NonNull RemoveNeighboursFromTaskConfig removeNeighboursFromTaskConfig)
	{
		repository().removeNeighboursFromTask(removeNeighboursFromTaskConfig);
		return false;
	}

	public void addSubTask(@NonNull Long idTask, @NonNull Long idSubTask) throws NotFoundException
	{
		repository().addSubTask(idTask, idSubTask);
	}

	public void addPredecessor(@NonNull Long idTask, @NonNull Long idSucTask) throws NotFoundException
	{
		repository().addPredecessor(idTask, idSucTask);
	}

	public void addSuccessor(@NonNull Long idTask, @NonNull Long idSubTask) throws NotFoundException
	{
		repository().addSuccessor(idTask, idSubTask);
	}

	public void removeSubTask(@NonNull Long idTask, @NonNull Long idSubTask) throws NotFoundException
	{
		repository().removeSubTask(idTask, idSubTask);
	}

	public void removePredecessor(@NonNull Long idTask, @NonNull Long idSubTask) throws NotFoundException
	{
		repository().removePredecessor(idTask, idSubTask);
	}

	public void removeSuccessor(@NonNull Long idTask, @NonNull Long idSubTask) throws NotFoundException
	{
		repository().removeSuccessor(idTask, idSubTask);
	}
}