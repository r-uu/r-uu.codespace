package de.ruu.app.jeeeraaah.backend.common.mapping.lazy.jpa;

import de.ruu.app.jeeeraaah.backend.common.mapping.Mappings;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA;
import de.ruu.app.jeeeraaah.common.api.domain.TaskEntity;
import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTOLazy;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

/** {@link TaskGroupJPA} -> {@link TaskGroupDTO} */
@Mapper public interface Map_Task_Lazy_JPA
{
	Map_Task_Lazy_JPA INSTANCE = Mappers.getMapper(Map_Task_Lazy_JPA.class);

	/**
	 * @param group necessary to propagate task group id to task bean in {@link #create(TaskGroupJPA, TaskLazy)},
	 *              the type of {@code group} must match the type of parameter {@code group] in {@link
	 *              #create(TaskGroupJPA, TaskLazy)} exactly
	 * @param in    the lazy task to be mapped
	 * @return      the mapped task bean
	 */
	@NonNull TaskJPA map(@NonNull TaskGroupJPA group, @NonNull TaskLazy in);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskLazy               in,
			@NonNull @MappingTarget TaskJPA                out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		// required arguments id, version and name are already set in constructor
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskLazy               in,
			@NonNull @MappingTarget TaskJPA                out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		// required arguments id, version and name are already set in constructor
	}

	/** mapstruct object factory */
	@ObjectFactory default @NonNull TaskJPA create(@NonNull TaskGroupJPA group, @NonNull TaskLazy in)
	{
		return new TaskJPA(group, in.name());
	}
}