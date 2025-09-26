package de.ruu.app.jeeeraaah.common.api.domain;

import lombok.NonNull;

import java.util.Set;

public interface TaskGroupLazy extends TaskGroupFlat
{
	@NonNull Set<Long> taskIds();
}