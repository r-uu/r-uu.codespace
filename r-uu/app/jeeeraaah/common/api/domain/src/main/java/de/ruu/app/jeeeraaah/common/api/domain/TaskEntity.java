package de.ruu.app.jeeeraaah.common.api.domain;

import de.ruu.lib.jpa.core.Entity;

import java.time.LocalDate;
import java.util.Optional;

/** combines {@link Entity} and {@link Task}, these carry the essential data for persistent tasks */

public interface TaskEntity<TG extends TaskGroupEntity<? extends TaskEntity<TG, ?>>, SELF extends TaskEntity<TG, SELF>>
		extends Task<TG, SELF>, Entity<Long>
{ }
