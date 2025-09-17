package de.ruu.app.jeeeraaah.common.api.domain;

import de.ruu.lib.jpa.core.Entity;

/** combines {@link Entity} and {@link TaskGroup}, these carry the essential data for persistent task groups  */
public interface TaskGroupEntity<T extends TaskEntity<?, ?>>
		extends TaskGroup<T>, Entity<Long> { }