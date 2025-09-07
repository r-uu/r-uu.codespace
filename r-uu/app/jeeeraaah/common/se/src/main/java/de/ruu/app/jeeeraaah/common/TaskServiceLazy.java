package de.ruu.app.jeeeraaah.common;

import de.ruu.app.jeeeraaah.common.dto.TaskLazy;
import lombok.NonNull;

import java.util.Set;

public interface TaskServiceLazy
{
	Set<TaskLazy> findTasksLazy(@NonNull Set<Long> ids) throws Exception;
}