package de.ruu.app.jeeeraaah.common.api.ws.rs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;
import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/** Transfer object for tasks with the ids of their related tasks and their task group. */
@JsonAutoDetect(fieldVisibility = ANY)
@EqualsAndHashCode
@ToString
@Getter                   // generate getter methods for all fields using lombok unless configured otherwise
@Accessors(fluent = true) // generate fluent accessors with lombok
public class TaskDTOLazy implements TaskLazy
{
	private @NonNull Long  id;
	private @NonNull Short version;

	private @NonNull  String    name;
	private @NonNull  Boolean   closed;
	private Optional<String>    description;
	private Optional<LocalDate> start;
	private Optional<LocalDate> end;

	private @NonNull  Long taskGroupId;
	private @Nullable Long superTaskId;

	private final @NonNull Set<Long> subTaskIds     = new HashSet<>();
	private final @NonNull Set<Long> predecessorIds = new HashSet<>();
	private final @NonNull Set<Long> successorIds   = new HashSet<>();

	///////////////
	// constructors
	///////////////

	public TaskDTOLazy() { this.closed = false; } // for jackson

	public TaskDTOLazy(TaskGroupLazy group, @NonNull String name)
	{
		this();
		this.taskGroupId = group.id();
		this.name        = name;
	}

	@Override public @NonNull TaskLazy description(@Nullable String description)
	{
		this.description = Optional.ofNullable(description);
		return this;
	}

	@Override public @NonNull TaskLazy start(@Nullable LocalDate startEstimated)
	{
		this.start = Optional.ofNullable(startEstimated);
		return this;
	}

	@Override	public @NonNull TaskLazy end(@Nullable LocalDate finishEstimated)
	{
		this.end = Optional.ofNullable(finishEstimated);
		return this;
	}

	@Override	public @NonNull TaskLazy closed(@NonNull Boolean closed)
	{
		this.closed = closed;
		return this;
	}

	public @NonNull Set<Long> subTaskIds    () { return Collections.unmodifiableSet(subTaskIds    ); }
	public @NonNull Set<Long> predecessorIds() { return Collections.unmodifiableSet(predecessorIds); }
	public @NonNull Set<Long> successorIds  () { return Collections.unmodifiableSet(successorIds  ); }

	//////////////////////////////////////////////////////////////////////////////////////////////
	// java bean style accessors for those who do not work with fluent style accessors (mapstruct)
	//////////////////////////////////////////////////////////////////////////////////////////////

	public @NonNull  String    getName()                                    { return name;                                           }
	public           void      setName       (@NonNull  String name)        {   this.name        = name;                             }
	public @NonNull  Boolean   getClosed()                                  { return closed;                                         }
	public           void      setClosed     (@NonNull  Boolean closed)     {   this.closed      = closed;                           }
	public Optional<String>    getDescription()                             { return description;                                    }
	public           void      setDescription(@Nullable String description) {   this.description = Optional.ofNullable(description); }
	public Optional<LocalDate> getStart()                                   { return start;                                          }
	public           void      setStart      (@Nullable LocalDate start)    {   this.start       = Optional.ofNullable(start);       }
	public Optional<LocalDate> getEnd()                                     { return end;                                            }
	public           void      setEnd        (@Nullable LocalDate end)      {   this.end         = Optional.ofNullable(end);         }

	//////////////////////
	// mapstruct callbacks
	//////////////////////
}