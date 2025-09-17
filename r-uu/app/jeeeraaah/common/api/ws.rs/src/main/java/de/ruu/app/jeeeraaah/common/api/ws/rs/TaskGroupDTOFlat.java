package de.ruu.app.jeeeraaah.common.api.ws.rs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupEntity;
import jakarta.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static java.util.Objects.requireNonNull;
import static java.util.UUID.randomUUID;

/** Transfer object for task groups without any information about their related tasks. */
// EqualsAndHashCode is for documenting the intent of manually created equals and hashCode methods
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false, doNotUseGetters = true)
@JsonAutoDetect(fieldVisibility = ANY)
@ToString
@Getter
@Accessors(fluent = true)
public class TaskGroupDTOFlat
{
	@EqualsAndHashCode.Exclude
	private final UUID jsonId = randomUUID();

	private @NonNull  Long   id;
	private @NonNull  Short  version;
	private @NonNull  String name;
	private @Nullable String description;

	protected TaskGroupDTOFlat() { } // for jackson

	public TaskGroupDTOFlat(@NonNull TaskGroupEntity<?> entity)
	{
		this();
		id          = requireNonNull(entity.id());
		version     = requireNonNull(entity.version());
		name        = entity.name();
		description = entity.description().orElse("");
	}

	@Override public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof TaskGroupDTOFlat other)) return false;

		if (this.id != null && other.id != null) return id.equals(other.id);
		// otherwise compare by jsonId (survives JSON serialization)
		return jsonId.equals(other.jsonId);
	}

	@Override public int hashCode() { return id != null ? id.hashCode() : jsonId.hashCode(); }
}