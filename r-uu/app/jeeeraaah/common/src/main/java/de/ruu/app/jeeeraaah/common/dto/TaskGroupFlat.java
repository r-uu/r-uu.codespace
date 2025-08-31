package de.ruu.app.jeeeraaah.common.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA;
import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;

// EqualsAndHashCode is for documenting the intent of manually created equals and hashCode methods
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false, doNotUseGetters = true)
/** Transfer object for task groups with the ids of their related tasks. */
@JsonAutoDetect(fieldVisibility = ANY)
@ToString
@Slf4j
@Getter
@Accessors(fluent = true)
public class TaskGroupFlat
{
	@EqualsAndHashCode.Exclude
	private final UUID jsonId = randomUUID();

	private @NonNull  Long   id;
	private @NonNull  Short  version;
	private @NonNull  String name;
	private @Nullable String description;

	protected TaskGroupFlat() { } // for jackson

	public TaskGroupFlat(@NonNull TaskGroupEntityJPA entity)
	{
		this();
		id          = requireNonNull(entity.id());
		version     = requireNonNull(entity.version());
		name        = entity.name();
		description = entity.description().orElse("");
	}

	public TaskGroupFlat(@NonNull TaskGroupBean bean)
	{
		this();
		id          = requireNonNull(bean.getId());
		version     = requireNonNull(bean.getVersion());
		name        = bean.name();
		description = bean.description().orElse("");
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof TaskGroupFlat other)) return false;

		if (this.id != null && other.id != null) return id.equals(other.id);
		// otherwise compare by jsonId (survives JSON serialization)
		return jsonId.equals(other.jsonId);
	}

	@Override public int hashCode() { return id != null ? id.hashCode() : jsonId.hashCode(); }
}