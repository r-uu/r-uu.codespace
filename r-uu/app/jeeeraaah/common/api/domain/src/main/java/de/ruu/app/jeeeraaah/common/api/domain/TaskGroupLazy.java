package de.ruu.app.jeeeraaah.common.api.domain;

import de.ruu.lib.jpa.core.Entity;
import lombok.NonNull;

import java.util.Set;

public interface TaskGroupLazy extends Entity<Long>
{
	@NonNull Long      id     ();
	@NonNull Set<Long> taskIds();
	@NonNull String    name   ();
}