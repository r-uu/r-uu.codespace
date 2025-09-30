package de.ruu.app.jeeeraaah.backend.persistence.jpa;

import de.ruu.app.jeeeraaah.common.api.domain.RemoveNeighboursFromTaskConfig;
import de.ruu.app.jeeeraaah.common.api.domain.TaskService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
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
		implements TaskService<TaskJPA>
{
	protected abstract TaskRepositoryJPA repository();

	@PostConstruct private void postConstruct() { log.debug("repository available: {}", not(isNull(repository()))); }

	@Override public @NonNull TaskJPA           create(@NonNull TaskJPA entity) { return repository().create(entity); }
	@Override public @NonNull Optional<TaskJPA> read  (@NonNull Long    id    ) { return repository().find  (id    ); }
	@Override public @NonNull TaskJPA           update(@NonNull TaskJPA entity)
			throws EntityNotFoundException
	                                                                            { return repository().update(entity); }
	@Override public          void              delete(@NonNull Long    id    )
			throws EntityNotFoundException
	                                                                            {        repository().delete(id    ); }

	@Override public @NonNull Set<TaskJPA> findAll() { return new HashSet<>(repository().findAll()); }

	@Override public Optional<TaskJPA>     findWithRelated(@NonNull Long id) { return repository().findWithRelated(id); }

	@Override public Set<TaskJPA>          findTasks(@NonNull Set<Long> ids) { return repository().findTasks(ids); }

	@Override public void addSubTask(@NonNull TaskJPA task, @NonNull TaskJPA subTask) throws NotFoundException
	{
		repository().addSubTask(task.id(), subTask.id());
	}

	@Override public void addPredecessor(@NonNull TaskJPA task, @NonNull TaskJPA predecessor)
	{
		repository().addPredecessor(task.id(), predecessor.id());
	}

	@Override public void addSuccessor(@NonNull TaskJPA task, @NonNull TaskJPA successor)
	{
		repository().addSuccessor(task.id(), successor.id());
	}

	@Override public void removeSuperSubTask(@NonNull TaskJPA task, @NonNull TaskJPA subTask)
	{
		repository().removeSubTask(task.id(), subTask.id());
	}

	@Override public void removePredecessor(@NonNull TaskJPA task, @NonNull TaskJPA predecessor)
	{
		repository().removePredecessor(task.id(), predecessor.id());
	}

	@Override public void removeSuccessor(@NonNull TaskJPA task, @NonNull TaskJPA successor)
	{
		repository().removeSuccessor(task.id(), successor.id());
	}

	@Override public void removeNeighboursFromTask(@NonNull RemoveNeighboursFromTaskConfig removeNeighboursFromTaskConfig)
	{
		repository().removeNeighboursFromTask(removeNeighboursFromTaskConfig);
	}

	public void addSubTask(@NonNull Long idTask, @NonNull Long idSubTask) throws TaskRelationException
	{
		repository().addSubTask(idTask, idSubTask);
	}

	public void addPredecessor(@NonNull Long idTask, @NonNull Long idSucTask) throws TaskRelationException
	{
		repository().addPredecessor(idTask, idSucTask);
	}

	public void addSuccessor(@NonNull Long idTask, @NonNull Long idSubTask) throws TaskRelationException
	{
		repository().addSuccessor(idTask, idSubTask);
	}

	public void removeSubTask(@NonNull Long idTask, @NonNull Long idSubTask) throws TaskRelationException
	{
		repository().removeSubTask(idTask, idSubTask);
	}

	public void removePredecessor(@NonNull Long idTask, @NonNull Long idSubTask) throws TaskRelationException
	{
		repository().removePredecessor(idTask, idSubTask);
	}

	public void removeSuccessor(@NonNull Long idTask, @NonNull Long idSubTask) throws TaskRelationException
	{
		repository() .removeSuccessor(idTask, idSubTask);
	}
}