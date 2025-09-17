package de.ruu.app.jeeeraaah.backend.common.mapping.jpa.lazy;

import de.ruu.app.jeeeraaah.backend.common.mapping.Mappings;
import de.ruu.app.jeeeraaah.backend.common.mapping.jpa.dto.Map_Task_JPA_DTO;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTOLazy;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTOLazy;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static java.util.Objects.isNull;

/** {@link TaskGroupJPA} -> {@link TaskGroupDTO} */
@Mapper public interface Map_Task_JPA_Lazy
{
	Map_Task_JPA_Lazy INSTANCE = Mappers.getMapper(Map_Task_JPA_Lazy.class);

	@NonNull
	TaskLazy map(@NonNull TaskJPA in, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskJPA                in,
			@NonNull @MappingTarget TaskLazy               out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		// required arguments id, version and name are already set in constructor
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskJPA                in,
			@NonNull @MappingTarget TaskLazy               out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		// required arguments id, version and name are already set in constructor
	}

	/** mapstruct object factory */
	@ObjectFactory default @NonNull TaskLazy create(@NonNull TaskJPA in)
	{
		return new TaskDTOLazy(Mappings.toLazy(in.taskGroup(), new ReferenceCycleTracking()), in.name());
	}
}