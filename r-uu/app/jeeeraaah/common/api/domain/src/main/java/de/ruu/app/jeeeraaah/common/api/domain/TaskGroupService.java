package de.ruu.app.jeeeraaah.common.api.domain;

import lombok.NonNull;

import java.util.Optional;
import java.util.Set;

public interface TaskGroupService<TG extends TaskGroup<?>>
{
	@NonNull TG            create(@NonNull TG   taskGroup) throws Exception;
	Optional<? extends TG> read  (@NonNull Long id       ) throws Exception;
	/**
	 * updates the task group if it is persistent (has an id) and can be found in the backend<p>
	 * if it is persistent but can not be found, it throws a TaskGroupNotFoundException<p>
	 * if it is not persistent, it creates a new task group in the backend
	 * @param taskGroup
	 * @return the updated or newly created task group
	 * @throws TaskGroupNotFoundException
	 */
	@NonNull TG            update(@NonNull TG   taskGroup) throws Exception;
	void                   delete(@NonNull Long id       ) throws Exception;

	@NonNull Set<? extends TG> findAll()                       throws Exception;
	Optional    <? extends TG> findWithTasks(@NonNull Long id) throws Exception;

	void removeFromGroup(@NonNull Long idGroup, @NonNull Long idTask) throws Exception;

	Optional<TaskGroupLazy> findGroupLazy(@NonNull Long id) throws Exception;
	Optional<TaskGroupFlat> findGroupFlat(@NonNull Long id) throws Exception;

	class TaskGroupNotFoundException extends RuntimeException
	{
		public TaskGroupNotFoundException(String text) { super(text); }
	}
}