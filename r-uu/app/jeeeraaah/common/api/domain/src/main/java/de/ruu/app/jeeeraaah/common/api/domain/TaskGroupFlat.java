package de.ruu.app.jeeeraaah.common.api.domain;

import de.ruu.lib.jpa.core.Entity;
import lombok.NonNull;

import java.util.Optional;

/**
 * A flat representation of a task group that doesn't include the full hierarchy of tasks.
 * This is useful for APIs that need to display or manipulate task groups without loading all related tasks.
 */
public interface TaskGroupFlat extends Entity<Long>, Comparable<TaskGroupFlat>
{
	@NonNull String  name();
	Optional<String> description();

	@NonNull TaskGroupFlat name        (@NonNull String name       );
	@NonNull TaskGroupFlat description(          String description);

	@Override default int compareTo(@NonNull TaskGroupFlat other) { return this.name().compareTo(other.name()); }
}