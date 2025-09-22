package de.ruu.app.jeeeraaah.backend.common.mapping.lazy.jpa;

import de.ruu.app.jeeeraaah.backend.common.mapping.jpa.dto.Map_Task_JPA_DTO;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
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
@Mapper public interface Map_TaskGroup_Lazy_JPA
{
	Map_TaskGroup_Lazy_JPA INSTANCE = Mappers.getMapper(Map_TaskGroup_Lazy_JPA.class);

	@NonNull TaskGroupJPA map(@NonNull TaskGroupLazy in);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(@NonNull TaskGroupLazy in, @NonNull @MappingTarget TaskGroupJPA out)
	{
		// required arguments id, version and name are already set in constructor
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(@NonNull TaskGroupLazy in, @NonNull @MappingTarget TaskGroupJPA out)
	{
		// required arguments id, version and name are already set in constructor
	}

	/** mapstruct object factory */
	@ObjectFactory default @NonNull TaskGroupJPA create(@NonNull TaskGroupLazy in) { return new TaskGroupJPA(in); }
}