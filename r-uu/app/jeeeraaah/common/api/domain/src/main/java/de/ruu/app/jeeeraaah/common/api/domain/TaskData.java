package de.ruu.app.jeeeraaah.common.api.domain;

import jakarta.annotation.Nullable;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Common interface for task data that can be shared across different implementations.
 *
 * @param <SELF> The implementing type to support method chaining
 */
public interface TaskData<SELF> extends Comparable<TaskData<SELF>>
{
	@NonNull String     name       ();
	Optional<String>    description();
	Optional<LocalDate> start      ();
	Optional<LocalDate> end        ();
	@NonNull Boolean    closed     ();

	@NonNull SELF name       (@NonNull  String    name);
	@NonNull SELF description(@Nullable String    description);
	@NonNull SELF start      (@Nullable LocalDate start);
	@NonNull SELF end        (@Nullable LocalDate end);
	@NonNull SELF closed     (@NonNull  Boolean   closed);

	@Override default int compareTo(TaskData other) {return this.name().compareTo(other.name());}
}
