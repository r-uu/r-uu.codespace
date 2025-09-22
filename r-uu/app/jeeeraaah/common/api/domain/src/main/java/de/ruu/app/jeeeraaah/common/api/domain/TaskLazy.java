package de.ruu.app.jeeeraaah.common.api.domain;

import de.ruu.lib.jpa.core.Entity;
import jakarta.annotation.Nullable;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public interface TaskLazy extends Entity<Long>, TaskData<TaskLazy>
{
	@NonNull TaskLazy name(@NonNull String name);

	@NonNull TaskLazy description(@Nullable String    description);
	@NonNull TaskLazy start      (@Nullable LocalDate startEstimated);
	@NonNull TaskLazy end        (@Nullable LocalDate finishEstimated);
	@NonNull TaskLazy closed     (@NonNull  Boolean   closed);

	@NonNull  Long taskGroupId();
	@Nullable Long superTaskId();

	@NonNull Set<Long> subTaskIds     = new HashSet<>();
	@NonNull Set<Long> predecessorIds = new HashSet<>();
	@NonNull Set<Long> successorIds   = new HashSet<>();
}