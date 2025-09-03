package de.ruu.app.jeeeraaah.common.jpa;

import de.ruu.app.jeeeraaah.common.TaskGroupService;
import de.ruu.app.jeeeraaah.common.TaskGroupServiceFlat;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupFlat;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupLazy;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/** TODO: rename and move this type (no JPA suffix) */
@Slf4j
public abstract class TaskGroupServiceJPA
		implements TaskGroupService<TaskGroupEntityJPA>, TaskGroupServiceFlat
{
	protected abstract TaskGroupRepositoryJPA repository();

//	@PostConstruct private void postConstruct() { log.debug("repository available: {}", not(isNull(repository()))); }

	@Override public @NonNull TaskGroupEntityJPA           create(@NonNull TaskGroupEntityJPA entity)
			{ return repository().save  (entity); }
	@Override public @NonNull Optional<TaskGroupEntityJPA> read  (@NonNull Long               id    )
			{ return repository().find  (id    ); }
	@Override public @NonNull TaskGroupEntityJPA           update(@NonNull TaskGroupEntityJPA entity)
			{ return repository().save  (entity); }
	@Override public          boolean                      delete(@NonNull Long               id    )
			{ return repository().delete(id    ); }

	@Override public @NonNull Set<TaskGroupEntityJPA>      findAll()
			{ return new HashSet<>(repository().findAll()); }
	@Override public          Optional<TaskGroupEntityJPA> findWithTasks(@NonNull Long id)
			{ return repository().findWithTasks(id); }
	@Override public @NonNull Set<TaskGroupFlat>           findAllFlat()
			{ return new HashSet<>(repository().findAllFlat()); }

	@Override public boolean removeFromGroup(@NonNull Long idGroup, @NonNull Long idTask)
			{ return repository().deleteFromGroup(idGroup, idTask); }

	@Override public Optional<TaskGroupLazy>               findGroupLazy(@NonNull Long id)
			{ return repository().findGroupLazy(id); }
}