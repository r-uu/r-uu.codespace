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
	private @Nullable String    description;
	private @Nullable LocalDate start;
	private @Nullable LocalDate end;
	private @NonNull  Boolean   closed;

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

	public @NonNull Set<Long> subTaskIds    () { return Collections.unmodifiableSet(subTaskIds    ); }
	public @NonNull Set<Long> predecessorIds() { return Collections.unmodifiableSet(predecessorIds); }
	public @NonNull Set<Long> successorIds  () { return Collections.unmodifiableSet(successorIds  ); }

	//////////////////////////////////////////////////////////////////////////////////////////////
	// java bean style accessors for those who do not work with fluent style accessors (mapstruct)
	//////////////////////////////////////////////////////////////////////////////////////////////

	public @NonNull  String    getName()                                    { return name;                      }
	public           void      setName       (@NonNull  String name)        {   this.name = name;               }
	public @Nullable String    getDescription()                             { return description;               }
	public           void      setDescription(@Nullable String description) {   this.description = description; }
	public @Nullable LocalDate getStart()                                   { return start;                     }
	public           void      setStart      (@Nullable LocalDate start)    {   this.start = start;             }
	public @Nullable LocalDate getEnd()                                     { return end;                       }
	public           void      setEnd        (@Nullable LocalDate end)      {   this.end = end;                 }
	public @NonNull  Boolean   getClosed()                                  { return closed;                    }
	public           void      setClosed     (@NonNull  Boolean closed)     {   this.closed = closed;           }

	//////////////////////
	// mapstruct callbacks
	//////////////////////
}