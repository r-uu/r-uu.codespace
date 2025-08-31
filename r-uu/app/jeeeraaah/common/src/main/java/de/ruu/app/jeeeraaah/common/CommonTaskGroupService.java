package de.ruu.app.jeeeraaah.common;

import de.ruu.app.jeeeraaah.common.dto.TaskGroupLazy;
import lombok.NonNull;

import java.util.Optional;
import java.util.Set;

public interface CommonTaskGroupService<TG extends TaskGroup<?>>
{
	Optional<? extends TG> create(@NonNull TG   taskGroup);
	Optional<? extends TG> read  (@NonNull Long id       );
	@NonNull           TG  update(@NonNull TG   taskGroup);
	boolean                delete(@NonNull Long id       );

	Set     <? extends TG> findAll();
	Optional<? extends TG> findWithTasks(@NonNull Long id);

	boolean removeFromGroup(@NonNull Long idGroup, @NonNull Long idTask);

	Optional<TaskGroupLazy> findGroupLazy(@NonNull Long id);
}