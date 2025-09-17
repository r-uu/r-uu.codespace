package de.ruu.app.jeeeraaah.common.api.domain;

import java.util.Set;

public interface TaskGroupServiceLazy
{
	Set<TaskGroupLazy> findAllLazy() throws Exception;
}