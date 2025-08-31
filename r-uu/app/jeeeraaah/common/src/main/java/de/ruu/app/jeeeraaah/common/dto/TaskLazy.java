package de.ruu.app.jeeeraaah.common.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA;
import de.ruu.app.jeeeraaah.common.map.lazy.bean.Map_Task_Lazy_Bean;
import de.ruu.lib.jpa.core.Entity;
import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

/** Transfer object for tasks with the ids of their related tasks. */
@JsonAutoDetect(fieldVisibility = ANY)
@EqualsAndHashCode
@ToString
@Slf4j
@Getter                   // generate getter methods for all fields using lombok unless configured otherwise
@Accessors(fluent = true) // generate fluent accessors with lombok
public class TaskLazy implements Entity<Long>
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

	public TaskLazy() { this.closed = false; } // for jackson

	public TaskLazy(TaskGroupLazy group, @NonNull String name)
	{
		this();
		this.taskGroupId = group.id();
		this.name        = name;
	}

//	public TaskLazy(@NonNull TaskEntityJPA entity)
//	{
//		id          = requireNonNull(entity.id());
//		version     = requireNonNull(entity.version());
//		name        =                entity.name();
//		description =                entity.description().orElse(null);
//		start       =                entity.start()      .orElse(null);
//		end         =                entity.end()        .orElse(null);
//		closed      =                entity.closed();
//
//		taskGroupId = requireNonNull(entity.taskGroup().id());
//
//		if (entity.superTask   ().isPresent()) superTaskId = entity.superTask().get().id();
//		else superTaskId = null;
//
//		if (entity.subTasks    ().isPresent())
//		{
//			entity.subTasks      ().get().forEach(t -> subTaskIds    .add(t.id()));
//		}
//		if (entity.predecessors().isPresent())
//		{
//			entity.predecessors  ().get().forEach(t -> predecessorIds.add(t.id()));
//		}
//		if (entity.successors  ().isPresent())
//		{
//			entity.successors    ().get().forEach(t -> successorIds  .add(t.id()));
//		}
//	}

	public TaskBean toBeanWithoutRelated(@NonNull TaskGroupBean groupBean)
	{
		return Map_Task_Lazy_Bean.INSTANCE.map(groupBean, this);
	}

	//////////////////////
	// mapstruct callbacks
	//////////////////////

	public void beforeMapping(@NonNull TaskEntityJPA in)
	{
		id          = in.id();
		version     = in.version();
		taskGroupId = in.taskGroup().id();
	}

	public void afterMapping(@NonNull TaskEntityJPA in)
	{
		if (in.superTask().isPresent())
		{
			superTaskId = in.superTask().get().id();
		}
		if (in.subTasks().isPresent())
		{
			Set<TaskEntityJPA> relatedTasks = in.subTasks().get();
			for (TaskEntityJPA relatedTask : relatedTasks)
			{
				subTaskIds.add(relatedTask.id());
			}
		}
		if (in.predecessors().isPresent())
		{
			Set<TaskEntityJPA> relatedTasks = in.predecessors().get();
			for (TaskEntityJPA relatedTask : relatedTasks)
			{
				predecessorIds.add(relatedTask.id());
			}
		}
		if (in.successors().isPresent())
		{
			Set<TaskEntityJPA> relatedTasks = in.successors().get();
			for (TaskEntityJPA relatedTask : relatedTasks)
			{
				successorIds.add(relatedTask.id());
			}
		}
	}

//	public static @NonNull Set<TaskBean> toMainTaskBeans(
//			@NonNull TaskGroupLazy groupLazy, @NonNull Set<TaskLazy> tasksLazy)
//	{
//		Set<      TaskBean> result        = new HashSet<>();
//		Map<Long, TaskLazy> tasksLazyById = new HashMap<>();
//		Map<Long, TaskBean> taskBeansById = new HashMap<>();
//
//		TaskGroupBean taskGroupBean = new TaskGroupBean(groupLazy);
//
//		ReferenceCycleTrackingWithMappingInfo context =
//				new ReferenceCycleTrackingWithMappingInfo(new MappingInfo(groupLazy, tasksLazy));
//
//		/*
//			 - for each task lazy try to look up a corresponding task bean in the context
//			 - if not found, create a new task bean from the lazy task
//		 */
//		xxx();
//
//		for (TaskLazy taskLazy : tasksLazy)
//		{
//			TaskBean taskBean = taskLazy.toBean(context);
//			taskBeansById.put(taskLazy.id(), taskBean);
//			tasksLazyById.put(taskLazy.id(), taskLazy);
//
//			if (not(taskBean.superTask().isPresent())) result.add(taskBean);
//		}
//
//		result.forEach(mainTask -> populateTaskBean(mainTask, tasksLazyById, taskBeansById));
//
//		return result;
//	}

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
}