package de.ruu.app.jeeeraaah.common.api.domain;

import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;
import lombok.NonNull;

import java.util.Set;

public interface TaskServiceLazy
{
	Set<TaskLazy> findTasksLazy(@NonNull Set<Long> ids) throws Exception;
}