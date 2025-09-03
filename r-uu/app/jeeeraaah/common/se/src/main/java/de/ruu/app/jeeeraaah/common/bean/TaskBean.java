package de.ruu.app.jeeeraaah.common.bean;

import de.ruu.app.jeeeraaah.common.Task;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskLazy;
import de.ruu.app.jeeeraaah.common.fx.TaskFXBean;
import de.ruu.app.jeeeraaah.common.map.Map_Task_Bean_EntityDTO;
import de.ruu.app.jeeeraaah.common.map.Map_Task_Bean_FXBean;
import de.ruu.lib.jpa.core.AbstractEntity;
import de.ruu.lib.jpa.core.Entity;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import de.ruu.lib.util.Strings;
import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.lang.System.identityHashCode;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static lombok.AccessLevel.NONE;

/** JavaBean for implementing business logic */
// EqualsAndHashCode is for documenting the intent of manually created equals and hashCode methods
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false, doNotUseGetters = true)
@ToString
@Slf4j
@Getter                   // generate getter methods for all fields using lombok unless configured otherwise
@Setter                   // generate setter methods for all fields using lombok unless configured otherwise
@Accessors(fluent = true) // generate fluent accessors with lombok and java-bean-style-accessors in non-abstract classes
                          // with ide, fluent accessors will (usually / by default) be ignored by mapstruct
public class TaskBean
		implements
				Task<TaskGroupBean, TaskBean>,
				Entity<Long>
{
	/**
	 * may be <pre>null</pre> if instance was not (yet) persisted.
	 * <p>may not be modified from outside type hierarchy (from non-{@link AbstractEntity}-subclasses)
	 * <p>not {@code final} or {@code @NonNull} because otherwise there has to be a constructor with {@code id}-parameter
	 */
	@EqualsAndHashCode.Include // documents intent of including id for equals() and hashCode() but both methods are
	                           // manually created
	@Nullable @Setter(NONE) private Long id;

	/** may be <pre>null</pre> if {@link AbstractEntity} was not (yet) persisted. */
	@Nullable @Setter(NONE) private Short version;

	/** mutable non-null */
	// no lombok-generation of setter because of additional validation in manually created method
	@Setter(NONE)
	@NonNull  private String    name;
	@Nullable private String    description;
	@Nullable private LocalDate start;
	@Nullable private LocalDate end;
	@NonNull  private Boolean   closed;

	/** mutable, but not nullable */
	@NonNull
	@EqualsAndHashCode.Exclude
	@ToString         .Exclude
	@Setter(NONE)
	private TaskGroupBean taskGroup;

	/** mutable nullable */
	@Nullable
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Getter(NONE) // provide handmade getter that returns optional
	@Setter(NONE) // provide handmade setter that handles bidirectional relation properly
	private TaskBean superTask;

	/**
	 * prevent direct access to this modifiable set from outside this class, use {@link #addSubTask(TaskBean)} and
	 * {@link #removeSubTask(TaskBean)} to modify the set
	 * <p>
	 * may explicitly be {@code null}, {@code null} indicates that there was no attempt to load related objects from db
	 * (lazy)
	 */
	@Nullable
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Getter(NONE) // provide handmade getter that returns unmodifiable
	@Setter(NONE) // no setter at all, use add method instead
	private Set<TaskBean> subTasks;

	/**
	 * prevent direct access to this modifiable set from outside this class, use {@link #addPredecessor(TaskBean)} and
	 * {@link #removePredecessor(TaskBean)} to modify the set
	 * <p>
	 * may explicitly be {@code null}, {@code null} indicates that there was no attempt to load related objects from db
	 * (lazy)
	 */
	@Nullable
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Getter(NONE) // provide handmade getter that returns unmodifiable
	@Setter(NONE) // no setter at all
	private Set<TaskBean> predecessors;

	/**
	 * prevent direct access to this modifiable set from outside this class, use {@link #addSuccessor(TaskBean)} and
	 * {@link #removeSuccessor(TaskBean)} to modify the set
	 * <p>
	 * may explicitly be {@code null}, {@code null} indicates that there was no attempt to load related objects from db
	 * (lazy)
	 */
	@Nullable
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@Getter(NONE) // provide handmade getter that returns unmodifiable
	@Setter(NONE) // no setter at all, use add method instead
	private Set<TaskBean> successors;

	private PreconditionCheckRelationalOperations preconditionCheckRelationalOperations; // lazy initialisation

	///////////////
	// constructors
	///////////////

	private TaskBean() { closed(false); }

	/** provide handmade required args constructor to properly handle relationships */
	public TaskBean(@NonNull TaskGroupBean taskGroup, @NonNull String name)
	{
		this();

		this.taskGroup = taskGroup;
		this.taskGroup.addTask(this);

		name(name);
	}

	/** provide handmade required args constructor to properly handle relationships */
	public TaskBean(@NonNull Entity<Long> entity, @NonNull TaskGroupBean taskGroup, @NonNull String name)
	{
		this();

		id      = entity.id();
		version = entity.version();

		this.taskGroup = taskGroup;
		this.taskGroup.addTask(this);

		name(name);
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof TaskBean other)) return false;
		if (id != null && other.id != null) return id.equals(other.id);
		return false;
	}

	@Override public int hashCode() { return (id != null) ? id.hashCode() : identityHashCode(this); }

	////////////////////////////////////////////////////////////////////////
	// fluent style accessors generated by lombok if not specified otherwise
	////////////////////////////////////////////////////////////////////////

	/**
	 * manually created fluent setter with extra parameter check (see throws documentation)
	 * @param name non-null, non-empty, non-blank
	 * @return {@code this}
	 * @throws IllegalArgumentException if {@code name} parameter is empty or blank
	 * @throws NullPointerException     if {@code name} parameter is {@code null}
	 */
	@NonNull public TaskBean name(@NonNull String name)
	{
		if (Strings.isEmptyOrBlank(name)) throw new IllegalArgumentException("name must not be empty nor blank");
		this.name = name;
		return this;
	}

	@Override @NonNull public TaskGroupBean  taskGroup() { return taskGroup; }
	@Override @NonNull public String         name     () { return name;      }

	@Override public Optional<String>    description() { return Optional.ofNullable(description); }
	@Override public Optional<LocalDate> start      () { return Optional.ofNullable(start      ); }
	@Override public Optional<LocalDate> end        () { return Optional.ofNullable(end        ); }

	@Override public Optional<TaskBean> superTask() { return Optional.ofNullable(superTask); }

	/** @return {@link #subTasks wrapped in unmodifiable */
	@Override public Optional<Set<TaskBean>> subTasks()
	{
		if (isNull(subTasks)) return Optional.empty();
		return Optional.of(Collections.unmodifiableSet(subTasks));
	}
	/** @return {@link #predecessors} wrapped in unmodifiable */
	@Override public Optional<Set<TaskBean>> predecessors()
	{
		if (isNull(predecessors)) return Optional.empty();
		return Optional.of(Collections.unmodifiableSet(predecessors));
	}
	/** @return {@link #successors} wrapped in unmodifiable */
	@Override public Optional<Set<TaskBean>> successors()
	{
		if (isNull(successors)) return Optional.empty();
		return Optional.of(Collections.unmodifiableSet(successors));
	}

	///////////////////////
	// additional accessors
	///////////////////////

	@Override public @NonNull TaskBean taskGroup(@NonNull TaskGroupBean taskGroup)
	{
		if (taskGroup.tasks().isPresent())
		{
			// create a new HashSet with most recent hash-codes even for elements that might be modified while this code is
			// running
			HashSet<TaskBean> tasksInGroup = new HashSet<>(taskGroup.tasks().get());
			if (tasksInGroup.contains(this)) return this; // do nothing
		}

		// remove this from current taskGroup's tasks, if it is already in there
		this.taskGroup.removeTask(this);
		// assign new taskGroup
		this.taskGroup = taskGroup;
		// add this to new taskGroup's tasks
		taskGroup.addTask(this);
		return this;
	}

	////////////////////////
	// relationship handling
	////////////////////////

	/** @throws IllegalArgumentException if {@code newSuperTask} is {@code this} */
	@Override public @NonNull TaskBean superTask(@Nullable TaskBean newSuperTask) throws IllegalArgumentException
	{
		if (newSuperTask == this) throw new IllegalArgumentException("newSuperTask must not be this");
		if (this.superTask == null && newSuperTask == null) return this; // no-op
		if (nonNull(this.superTask))
		{
			this.superTask.removeSubTask(this); // remove this from current superTask's children
		}
		if (nonNull(newSuperTask))
		{
			newSuperTask.addSubTask(this);      // add    this to new newSuperTask's children
		}
		this.superTask = newSuperTask;        // update this.superTask to new superTask
		return this;
	}

	@Override public boolean addSubTask(@NonNull TaskBean task)
	{
		if (preconditionCheckRelationalOperations().canBeAddedAsSubTask(task))
		{
			// update bidirectional relation
			task.superTask = this;
			return nonNullSubTasks().add(task);
		}
//		else log.warn("failure adding {} to children, check logs for reason", task);
		return false;
	}

	@Override public boolean addPredecessor(@NonNull TaskBean task)
	{
		if (preconditionCheckRelationalOperations().canBeAddedAsPredecessor(task))
		{
			// update bidirectional relation
			if (task.nonNullSuccessors().add(this)) return nonNullPredecessors().add(task);
			else
			{
				// this might already be among predecessors of task, if so return true
				if (task.nonNullPredecessors().contains(this)) return true;
			}
		}
//		else log.warn("failure adding {} to predecessors, check logs for reason", task);
		return false;
	}

	@Override public boolean addSuccessor(@NonNull TaskBean task)
	{
		if (preconditionCheckRelationalOperations().canBeAddedAsSuccessor(task))
		{
			// update bidirectional relation
			if (task.nonNullPredecessors().add(this)) return nonNullSuccessors().add(task);
			else
			{
				// this might already be among successors of task, if so return true
				if (task.nonNullSuccessors().contains(this)) return true;
			}
		}
//		else log.warn("failure adding {} to successors, check logs for reason", task);
		return false;
	}

	@Override public boolean removeSubTask(@NonNull TaskBean subTask)
	{
		if (nonNull(subTask.superTask))
			if (subTask.superTask.equals(this))
				if (nonNull(subTasks))
				{
					TaskBean superTask = subTask.superTask; // remember parent in case removal has to be rolled back

					subTask.superTask = null;               // remove superTask in subTask
					boolean result = subTasks.remove(subTask);
					if (result == false) subTask.superTask = superTask; // rollback removal (reset superTask in sub task)
					return result;
				}
				else
					throw new IllegalStateException("no sub tasks exist, subTask id: " + subTask.id());
			else
				throw new IllegalArgumentException("wrong super task, subTask.superTask is not equal to this, entity id: " + subTask.id());
		else
			throw new IllegalStateException("no super task exists, subTask id: " + subTask.id());
	}

	@Override public boolean removePredecessor(@NonNull TaskBean predecessor)
	{
		if (nonNull(predecessor.successors))
		{
			if (predecessor.successors.remove(this)) return predecessors.remove(predecessor);
		}
		throw new IllegalStateException("could not remove this from successors of task");
	}

	@Override public boolean removeSuccessor(@NonNull TaskBean successor)
	{
		if (nonNull(successor.predecessors))
		{
			if (successor.predecessors.remove(this)) return successors.remove(successor);
		}
		throw new IllegalStateException("could not remove this from predecessors of task");
	}

	public @NonNull TaskEntityDTO toDTO(ReferenceCycleTracking context)
	{
		return Map_Task_Bean_EntityDTO.INSTANCE.map(this, context);
	}

	///////////////////////
	// additional accessors
	///////////////////////

	//////////////////////
	// mapstruct callbacks
	//////////////////////

	public void beforeMapping(@NonNull TaskEntityDTO in, @NonNull ReferenceCycleTracking context)
	{
		id      = in.id();
		version = in.version();
		// mapping of other fields is done via mapstruct using java-beans accessors
	}

	public void afterMapping(@NonNull TaskEntityDTO in, @NonNull ReferenceCycleTracking context)
	{
		if (in.superTask().isPresent())
		{
			TaskEntityDTO relatedTask       = in.superTask().get();
			// check if related task was already mapped
			TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
			if (isNull(relatedTaskMapped))
				// start new mapping for related task
				superTask(relatedTask.toBean(context));
			else
				// use already mapped related task
				superTask(relatedTaskMapped);
		}
		if (in.subTasks().isPresent())
		{
			Set<TaskEntityDTO> relatedTasks = in.subTasks().get();
			for (TaskEntityDTO relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
				if (isNull(relatedTaskMapped))
					// start new mapping for related task
					addSubTask(relatedTask.toBean(context));
				else
					// use already mapped related task
					addSubTask(relatedTaskMapped);
			}
		}
		if (in.predecessors().isPresent())
		{
			Set<TaskEntityDTO> relatedTasks = in.predecessors().get();
			for (TaskEntityDTO relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
				if (isNull(relatedTaskMapped))
					// start new mapping for related task
					addPredecessor(relatedTask.toBean(context));
				else
					// use already mapped related task
					addPredecessor(relatedTaskMapped);
			}
		}
		if (in.successors().isPresent())
		{
			Set<TaskEntityDTO> relatedTasks = in.successors().get();
			for (TaskEntityDTO relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
				if (isNull(relatedTaskMapped))
					// start new mapping for related task
					addSuccessor(relatedTask.toBean(context));
				else
					// use already mapped related task
					addSuccessor(relatedTaskMapped);
			}
		}
	}

	/**
	 * Maps optional return values of {@link TaskFXBean} field accessors to java bean style fields. This cannot be done
	 * by mapstruct automatically.
	 * <p>
	 * In addition, this method is used to set the id and version of the task bean.
	 */
	public void beforeMapping(@NonNull TaskFXBean in, @NonNull ReferenceCycleTracking context)
	{
		id      = in.id();
		version = in.version();

		in.description().ifPresent(p -> description(p));
		// mapping of other fields is done via mapstruct using java-beans accessors
	}

	public void afterMappingFX(@NonNull TaskFXBean in, @NonNull ReferenceCycleTracking context)
	{
		if (in.superTask().isPresent())
		{
			TaskFXBean relatedTask       = in.superTask().get();
			// check if related task was already mapped
			TaskBean   relatedTaskMapped = context.get(relatedTask, TaskBean.class);
			if (isNull(relatedTaskMapped))
					// start new mapping for related task
					superTask(relatedTask.toBean(context));
			else
					// use already mapped related task
					superTask(relatedTaskMapped);
		}
		if (in.subTasks().isPresent())
		{
			Set<TaskFXBean> relatedTasks = in.subTasks().get();
			for (TaskFXBean relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						addSubTask(relatedTask.toBean(context));
				else
						// use already mapped related task
						addSubTask(relatedTaskMapped);
			}
		}
		if (in.predecessors().isPresent())
		{
			Set<TaskFXBean> relatedTasks = in.predecessors().get();
			for (TaskFXBean relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						addPredecessor(relatedTask.toBean(context));
				else
						// use already mapped related task
						addPredecessor(relatedTaskMapped);
			}
		}
		if (in.successors().isPresent())
		{
			Set<TaskFXBean> relatedTasks = in.successors().get();
			for (TaskFXBean relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						addSuccessor(relatedTask.toBean(context));
				else
						// use already mapped related task
						addSuccessor(relatedTaskMapped);
			}
		}
	}

	public void beforeMapping(
			@NonNull TaskGroupBean groupBean,
			@NonNull TaskLazy      in)
	{
		id      = in.id();
		version = in.version();

		taskGroup(groupBean);
		// mapping of other fields is done via mapstruct using java-beans accessors
	}

	public void afterMapping(
			@NonNull TaskGroupBean groupBean,
			@NonNull TaskLazy      in)
	{
//		log.debug("lazy in task {}", in.name());
//		if (not(superTask().isPresent()) && nonNull(in.superTaskId()))
//		{
//			// fetch the related task from the mappingInfo
//			TaskLazy relatedTaskLazy   = context.get(in.superTaskId());
//			// check if related task was already mapped
//			TaskBean relatedTaskMapped = context.get(relatedTaskLazy, TaskBean.class);
//			if (isNull(relatedTaskMapped))
//				// start new mapping for related task
//				superTask(relatedTaskLazy.toBeanWithoutRelated(context));
//			else
//				// use already mapped related task
//				superTask(relatedTaskMapped);
//		}
//
//		for (Long subTaskId : in.subTaskIds())
//		{
//			// fetch the related task from the mappingInfo
//			TaskLazy relatedTaskLazy   = mappingInfo.tasksById().get(subTaskId);
//			// check if related task was already mapped
//			TaskBean relatedTaskMapped = context.get(relatedTaskLazy, TaskBean.class);
//			if (isNull(relatedTaskMapped))
//				// start new mapping for related task
//				addSubTask(relatedTaskLazy.toBeanWithoutRelated(context));
//			else
//				// use already mapped related task
//				addSubTask(relatedTaskMapped);
//		}
//
//		for (Long predecessorId : in.predecessorIds())
//		{
//			// fetch the related task from the mappingInfo
//			TaskLazy relatedTaskLazy   = mappingInfo.tasksById().get(predecessorId);
//			// check if related task was already mapped
//			TaskBean relatedTaskMapped = context.get(relatedTaskLazy, TaskBean.class);
//			if (isNull(relatedTaskMapped))
//				// start new mapping for related task
//				addPredecessor(relatedTaskLazy.toBeanWithoutRelated(context));
//			else
//				// use already mapped related task
//				addPredecessor(relatedTaskMapped);
//		}
//
//		for (Long successorId : in.successorIds())
//		{
//			// fetch the related task from the mappingInfo
//			TaskLazy relatedTaskLazy   = mappingInfo.tasksById().get(successorId);
//			// check if related task was already mapped
//			TaskBean relatedTaskMapped = context.get(relatedTaskLazy, TaskBean.class);
//			if (isNull(relatedTaskMapped))
//				// start new mapping for related task
//				addSuccessor(relatedTaskLazy.toBeanWithoutRelated(context));
//			else
//				// use already mapped related task
//				addSuccessor(relatedTaskMapped);
//		}
	}

	public @NonNull TaskFXBean toFXBean(ReferenceCycleTracking context)
	{
		return Map_Task_Bean_FXBean.INSTANCE.map(this, context);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////
	// java bean style accessors for those who do not work with fluent style accessors (mapstruct)
	//////////////////////////////////////////////////////////////////////////////////////////////

	@NonNull
	public String    getName                                      () { return name;                      }
	public void      setName       (@NonNull String     name       ) {   name(name);                     }

	@Nullable
	public String    getDescription                               () { return description;               }
	public void      setDescription(@Nullable String    description) {   this.description = description; }

	@Nullable
	public LocalDate getStart                                     () { return start;                     }
	public void      setStart      (@Nullable LocalDate start      ) {   this.start       = start;       }

	@Nullable
	public LocalDate getEnd                                       () { return end;                       }
	public void      setEnd        (@Nullable LocalDate end        ) {   this.end         = end;         }

	public boolean   getClosed                                    () { return closed;                    }
	public void      setClosed     (          boolean   closed     ) {   this.closed      = closed;      }

	@NonNull private Set<TaskBean> nonNullSubTasks()
	{
		if (isNull(subTasks)) subTasks = new HashSet<>();
		return subTasks;
	}

	@NonNull private Set<TaskBean> nonNullPredecessors()
	{
		if (isNull(predecessors)) predecessors = new HashSet<>();
		return predecessors;
	}

	@NonNull private Set<TaskBean> nonNullSuccessors()
	{
		if (isNull(successors)) successors = new HashSet<>();
		return successors;
	}

	/** {@code null} safe check for containment */
	private boolean predecessorsContains(TaskBean bean)
	{
		if (isNull(predecessors)) return false;
		return predecessors.contains(bean);
	}

	/** {@code null} safe check for containment */
	private boolean successorsContains(TaskBean bean)
	{
		if (isNull(successors)) return false;
		return successors.contains(bean);
	}

	private PreconditionCheckRelationalOperations preconditionCheckRelationalOperations()
	{
		if (preconditionCheckRelationalOperations == null)
				preconditionCheckRelationalOperations = new PreconditionCheckRelationalOperations(this);
		return preconditionCheckRelationalOperations;
	}
}