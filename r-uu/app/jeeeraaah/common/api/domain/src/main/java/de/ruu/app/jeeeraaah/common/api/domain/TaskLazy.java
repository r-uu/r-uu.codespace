package de.ruu.app.jeeeraaah.common.api.domain;

import de.ruu.lib.jpa.core.Entity;
import jakarta.annotation.Nullable;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

public interface TaskLazy extends Entity<Long>, TaskData<TaskLazy>
{
	@NonNull String name();

	@NonNull  Long taskGroupId();
	@Nullable Long superTaskId();

	@NonNull Set<Long> subTaskIds     = new HashSet<>();
	@NonNull Set<Long> predecessorIds = new HashSet<>();
	@NonNull Set<Long> successorIds   = new HashSet<>();
}