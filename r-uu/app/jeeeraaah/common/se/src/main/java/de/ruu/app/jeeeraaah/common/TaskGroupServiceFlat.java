package de.ruu.app.jeeeraaah.common;

import de.ruu.app.jeeeraaah.common.dto.TaskGroupFlat;

import java.util.Set;

public interface TaskGroupServiceFlat
{
	Set<TaskGroupFlat> findAllFlat() throws Exception;
}