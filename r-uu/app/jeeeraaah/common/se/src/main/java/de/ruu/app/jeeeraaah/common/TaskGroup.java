package de.ruu.app.jeeeraaah.common;

import jakarta.annotation.Nullable;
import lombok.NonNull;

import java.util.Optional;
import java.util.Set;

/**
 * Generic, technology (JPA, JSONB, JAXB, MapStruct, ...) agnostic interface for task groups. Task groups contain {@link
 * Task}s that share a common concept such as being part of the tasks that have to be completed to complete a feature.
 * @param <T> {@link Task} implementation for {@link #tasks()} belonging to this task group instance
 */
public interface TaskGroup<T extends Task<?, ?>>
{
	@NonNull String           name();
	@NonNull TaskGroup<T>     name(@NonNull String name);
	         Optional<String> description();
	@NonNull TaskGroup<T>     description(@Nullable String description);
	         Optional<Set<T>> tasks();

//	/**
//	 * Adds a task to the internal set of tasks ({@link #tasks()}). A task always belongs to a task group. The internal
//	 * set of tasks for a task group has to be updated by a call to this method.
//	 * @param task task to be added to internal set of tasks
//	 * @return {@code true} id successful, {@code false} otherwise
//	 */
//	boolean addTask   (T task);
	/**
	 * Removes a task from the internal set of tasks ({@link #tasks()}). A task always belongs to a task group. The internal
	 * set of tasks for a task group has to be updated by a call to this method.
	 * @param task task to be removed from internal set of tasks
	 * @return {@code true} id successful, {@code false} otherwise
	 */
	boolean removeTask(T task);
}