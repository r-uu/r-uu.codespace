package de.ruu.app.jeeeraaah.backend.persistence.jpa;

import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupFlat;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupService;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupServiceLazy;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Set;

/** TODO: rename and move this type (no JPA suffix) */
@Slf4j
public abstract class TaskGroupServiceJPA
		implements TaskGroupService<TaskGroupJPA>, TaskGroupServiceLazy
{
	protected abstract TaskGroupRepositoryJPA repository();
	private final      TaskGroupRepositoryJPA repository = repository();

//	@PostConstruct private void postConstruct() { log.debug("repository available: {}", not(isNull(repository()))); }

	@Override public @NonNull TaskGroupJPA  create(@NonNull TaskGroupJPA entity) { return repository.create(entity); }
	@Override public Optional<TaskGroupJPA> read  (@NonNull Long        id     ) { return repository.find  (id    ); }
	@Override public @NonNull TaskGroupJPA  update(@NonNull TaskGroupJPA entity) { return repository.update(entity); }
	@Override public          void          delete(@NonNull Long id            ) {        repository.delete(id    ); }

	@Override public @NonNull Set<TaskGroupJPA>       findAll      (                ) { return repository.findAll      (  ); }
	@Override public          Optional<TaskGroupJPA>  findWithTasks(@NonNull Long id) { return repository.findWithTasks(id); }
	@Override public @NonNull Set<TaskGroupLazy>      findAllLazy  (                )	{	return repository.findAllLazy  (  ); }
	@Override public          Optional<TaskGroupLazy> findGroupLazy(@NonNull Long id) { return repository.findLazy     (id); }
	@Override public          Optional<TaskGroupFlat> findGroupFlat(@NonNull Long id) { return repository.findFlat     (id); }

	@Override public void removeFromGroup(@NonNull Long idGroup, @NonNull Long idTask)
			{ repository.deleteFromGroup(idGroup, idTask); }

	public @NonNull Set<TaskGroupFlat> findAllFlat() { return repository.findAllFlat(); }
}