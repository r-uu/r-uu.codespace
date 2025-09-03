package de.ruu.app.jeeeraaah.common.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import de.ruu.app.jeeeraaah.common.Task;
import de.ruu.app.jeeeraaah.common.TaskGroup;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA;
import de.ruu.app.jeeeraaah.common.map.Map_TaskGroup_EntityDTO_Bean;
import de.ruu.app.jeeeraaah.common.map.Map_TaskGroup_EntityDTO_EntityJPA;
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

import java.io.Serial;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static de.ruu.lib.util.BooleanFunctions.not;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.NONE;

/** data transfer object for fully serialised object graphs (including circular references) */
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="jsonId", scope = TaskGroupEntityDTO.class)
@JsonAutoDetect(fieldVisibility = ANY)
// EqualsAndHashCode is for documenting the intent of manually created equals and hashCode methods
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false, doNotUseGetters = true)
@ToString
@Slf4j
@Getter                   // generate getter methods for all fields using lombok unless configured otherwise ({@code
@Setter                   // generate setter methods for all fields using lombok unless configured otherwise ({@code
@Accessors(fluent = true) // generate fluent accessors with lombok and java-bean-style-accessors in non-abstract classes
                          // with ide, fluent accessors will (usually / by default) be ignored by mapstruct
public class TaskGroupEntityDTO
		implements
				TaskGroup<TaskEntityDTO>,
				Entity<Long>
{
	@Serial private static final long serialVersionUID = 1L;

	@EqualsAndHashCode.Exclude
	private final UUID jsonId = randomUUID();

	/**
	 * may be <pre>null</pre> if instance was not (yet) persisted.
	 * <p>not {@code final} or {@code @NonNull} because otherwise there has to be a constructor with {@code id}-parameter
	 */
	@EqualsAndHashCode.Include // documents intent of including id for equals() and hashCode() but both methods are
	                           // manually created
	@Nullable @Setter(NONE)
	private Long   id;

	/** may be <pre>null</pre> if {@link AbstractEntity} was not (yet) persisted. */
	@Nullable @Setter(NONE)
	private Short  version;

	/** mutable non-null */
	// no lombok-generation of setter because of additional validation in manually created method
	@NonNull @Setter(NONE)
	private String name;

	/** mutable nullable */
	@Nullable
	private String description;

	/**
	 * Prevent direct access to this modifiable set from outside this class.
	 * <p>
	 * may explicitly be {@code null}, {@code null} indicates that there was no attempt to load related objects from db
	 * (lazy)
	 */
	@Nullable
	@EqualsAndHashCode.Exclude
	@ToString         .Exclude
	@Getter(NONE) // provide handmade getter that returns unmodifiable
	@Setter(NONE) // no setter at all
//	@JsonManagedReference(value = "taskGroup-task")
	private Set<TaskEntityDTO> tasks;

	///////////////
	// constructors
	///////////////

	protected TaskGroupEntityDTO() { } // for jackson

	/**
	 * hand-made required args constructor to guarantee usage of hand made accessors.
	 * @param name non-empty name
	 */
	public TaskGroupEntityDTO(@NonNull String name) { name(name); }

	/**
	 * create a new task group entity from an existing entity
	 * <p>
	 * This constructor is used by mapstruct to create a new task group entity from an existing one.
	 * @param in   the existing entity, must not be {@code null}
	 * @param name non-empty name, must not be {@code null}, empty or blank
	 */
	public TaskGroupEntityDTO(@NonNull de.ruu.lib.jpa.core.Entity<Long> in, @NonNull String name)
	{
		this.id      = in.id();
		this.version = in.version();
		name(name);
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof TaskGroupEntityDTO other)) return false;

		if (this.id != null && other.id != null) return id.equals(other.id);
		// otherwise compare by jsonId (survives JSON serialization)
		return jsonId.equals(other.jsonId);
	}

	@Override public int hashCode() { return id != null ? id.hashCode() : jsonId.hashCode(); }

	public boolean equalsWithFieldsIgnoreIds(TaskGroupEntityDTO other)
	{
		if (equalsIdentity(other)) return true;

		// compare fields one by one but skip id and jsonId
		if (!Objects.equals(name       , other.name       )) return false;
		if (!Objects.equals(description, other.description)) return false;

		return true;
	}

	public boolean equalsWithFieldsIgnoreJsonId(TaskGroupEntityDTO other)
	{
		// equalsIdentity(other) is called inside equalsWithFieldsIgnoreIds(other)
		if (!equalsWithFieldsIgnoreIds(other)) return false;

		if (!Objects.equals(id, other.id)) return false;

		return true;
	}

	public boolean equalsWithFieldsIgnoreId(TaskGroupEntityDTO other)
	{
		// equalsIdentity(other) is called inside equalsWithFieldsIgnoreIds(other)
		if (!equalsWithFieldsIgnoreIds(other)) return false;

		if (!Objects.equals(jsonId, other.jsonId)) return false;

		return true;
	}

	public boolean equalsWithFields(TaskGroupEntityDTO other)
	{
		// equalsIdentity(other) is called inside equalsWithFieldsIgnoreIds(other)
		if (!equalsWithFieldsIgnoreIds(other)) return false;

		// compare id fields
		if (!Objects.equals(id    , other.id    )) return false;
		if (!Objects.equals(jsonId, other.jsonId)) return false;

		return true;
	}

	public boolean equalsIdentity(TaskGroupEntityDTO other)
	{
		if (this == other) return true;
		return false;
	}

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
	@Override @NonNull public TaskGroupEntityDTO name(@NonNull String name)
	{
		if (Strings.isEmptyOrBlank(name)) throw new IllegalArgumentException("name must not be empty nor blank");
		this.name = name;
		return this;
	}

	@Override public Optional<String> description() { return Optional.ofNullable(description); }

	/** @return optional unmodifiable */
	@Override public Optional<Set<TaskEntityDTO>> tasks()
	{
		if (isNull(tasks)) return Optional.empty();
		return Optional.of(Collections.unmodifiableSet(tasks));
	}

	//////////////////////////////////////////////////////////////////////////////////////////////
	// java bean style accessors for those who do not work with fluent style accessors (mapstruct)
	//////////////////////////////////////////////////////////////////////////////////////////////

	@Override public @Nullable Long  getId     () { return id; }
	@Override public @Nullable Short getVersion() { return version; }

	// do _NOT_ define getter for tasks to avoid handling of tasks by mapstruct automatism
//	@Nullable public Set<TaskEntityDTO> getTasks() { return tasks; }

	@NonNull public String getName()                     { return name(); }
	         public void   setName(@NonNull String name) { name(name); }

	@Nullable public String getDescription()                             { return description().orElse(null); }
	          public void   setDescription(@Nullable String description) { description(description); }

	///////////////////////
	// additional accessors
	///////////////////////

	public Optional<Set<TaskEntityDTO>> mainTaskEntityDTOs()
	{
		Set<TaskEntityDTO> result = new HashSet<>();

		Optional<Set<TaskEntityDTO>> optionalTasks = tasks();
		if (optionalTasks.isPresent())
		{
			Set<TaskEntityDTO> tasks = optionalTasks.get();
			for (TaskEntityDTO task : tasks)
			{
				if (not(task.superTask().isPresent())) result.add(task);
			}
		}

		if (not(result.isEmpty())) return Optional.of(result);
		return Optional.empty();
	}

	////////////////////////
	// relationship handling
	////////////////////////

	/**
	 * call this method from {@link TaskEntityDTO} constructor and {@link TaskEntityDTO#taskGroup(TaskGroupEntityDTO)} only
	 * @param task the {@link Task} to be added as task
	 * @return {@code true} if the task was added, {@code false} if the task was already present in this task group
	 */
	protected boolean addTask(@NonNull TaskEntityDTO task) { return nonNullTasks().add(task); }

	@Override public boolean removeTask(@NonNull TaskEntityDTO dto)
	{
		if (nonNull(tasks)) return tasks.remove(dto);
		return false;
	}

	public @NonNull TaskGroupEntityJPA toEntity(ReferenceCycleTracking context)
	{
		return Map_TaskGroup_EntityDTO_EntityJPA.INSTANCE.map(this, context);
	}

	public @NonNull TaskGroupBean toBean(ReferenceCycleTracking context)
	{
		return Map_TaskGroup_EntityDTO_Bean.INSTANCE.map(this, context);
	}

	//////////////////////
	// mapstruct callbacks
	//////////////////////

	public void beforeMapping(@NonNull TaskGroupEntityJPA in, @NonNull ReferenceCycleTracking context)
	{
		// set fields that cannot be modified from outside
		id      = in.getId();
		version = in.getVersion();
		// mapping of other fields is done via mapstruct using java-beans accessors
	}
	public void afterMapping(@NonNull TaskGroupEntityJPA in, @NonNull ReferenceCycleTracking context)
	{
		if (in.tasks().isPresent())
		{
			Set<TaskEntityJPA> relatedTasks = in.tasks().get();
			for (TaskEntityJPA relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskEntityDTO relatedTaskMapped = context.get(relatedTask, TaskEntityDTO.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						relatedTaskMapped = relatedTask.toDTO(context);
			}
		}
	}

	public void beforeMapping(@NonNull TaskGroupBean in, @NonNull ReferenceCycleTracking context)
	{
		// set fields that cannot be modified from outside
		id      = in.id     ();
		version = in.version();
		// mapping of other fields is done via mapstruct using java-beans accessors
		// task dto will be added to this task groups task during creation of task dto
	}

	public void afterMapping(@NonNull TaskGroupBean in, @NonNull ReferenceCycleTracking context)
	{
		if (in.tasks().isPresent())
		{
			Set<TaskBean> relatedTasks = in.tasks().get();
			for (TaskBean relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskEntityDTO relatedTaskMapped = context.get(relatedTask, TaskEntityDTO.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						relatedTaskMapped = relatedTask.toDTO(context);
			}
		}
	}

	private @NonNull Set<TaskEntityDTO> nonNullTasks()
	{
		if (isNull(tasks)) tasks = new HashSet<>();
		return tasks;
	}
}