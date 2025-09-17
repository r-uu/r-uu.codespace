package de.ruu.app.jeeeraaah.backend.persistence.jpa;

import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupService;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupServiceLazy;
import de.ruu.app.jeeeraaah.common.api.domain.TaskService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/** TODO: rename and move this type (no JPA suffix) */
@Slf4j
public abstract class TaskGroupServiceJPA
		implements TaskGroupService<TaskGroupJPA>, TaskGroupServiceLazy
{
	protected abstract TaskGroupRepositoryJPA repository();

//	@PostConstruct private void postConstruct() { log.debug("repository available: {}", not(isNull(repository()))); }

	@Override public @NonNull TaskGroupJPA           create(@NonNull TaskGroupJPA entity)
			{ return repository().create(entity); }

	@Override public Optional<TaskGroupJPA> read  (@NonNull Long        id    )
			{ return repository().find  (id    ); }

	@Override public @NonNull TaskGroupJPA           update(@NonNull TaskGroupJPA entity)
	{
		return repository().update(entity.getId(), entity.getVersion(), managed ->
		{
			// Update allowed mutable fields; rely on JPA dirty checking
			managed.setName(entity.getName());
			managed.setDescription(entity.getDescription());
		});
	}

	@Override public          void                   delete       (@NonNull Long id)
			throws TaskGroupNotFoundException
			{       repository().delete(id     ); }

	@Override public @NonNull Set<TaskGroupJPA>      findAll()
			{ return new HashSet<>(repository().findAll()); }

	@Override public          Optional<TaskGroupJPA> findWithTasks(@NonNull Long id)
			{ return repository().findWithTasks(id); }

	@Override public @NonNull Set<TaskGroupLazy>     findAllLazy()
	{
		return repository().findAllLazy();
	}

	@Override public void                            removeFromGroup(@NonNull Long idGroup, @NonNull Long idTask)
			throws TaskGroupNotFoundException, TaskService.TaskNotFoundException
			{        repository().deleteFromGroup(idGroup, idTask); }

	@Override public Optional<TaskGroupLazy>         findGroupLazy(@NonNull Long id)
			{ return repository().findLazy(id); }
}