package de.ruu.app.jeeeraaah.common.api.mapping.bean.lazy;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.api.domain.Task;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;
import de.ruu.app.jeeeraaah.common.api.mapping.Mappings;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTOLazy;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

import static de.ruu.app.jeeeraaah.common.api.mapping.Mappings.toLazy;

/** {@link TaskDTOLazy} -> {@link TaskBean} */
@Mapper public interface Map_Task_Bean_Lazy
{
	Map_Task_Bean_Lazy INSTANCE = Mappers.getMapper(Map_Task_Bean_Lazy.class);

	/**
	 * @param in    the task bean to be mapped
	 * @return      the mapped lazy task
	 */
	@Mapping(target = "description", source = "in.description") // solve ambiguous mapping group.description -> in.description
	@NonNull TaskLazy map(@NonNull TaskBean in);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping
	default void beforeMapping(
			@NonNull                TaskBean in,
			@NonNull @MappingTarget TaskLazy out)
	{
		// required arguments are set in constructor
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping
	default void afterMapping(
			@NonNull                TaskBean in,
			@NonNull @MappingTarget TaskLazy out)
	{
		// no manual mappings in addition to those done by mapstruct
	}

	/** mapstruct object factory */
	@ObjectFactory default @NonNull TaskLazy create(@NonNull TaskBean in)
	{
		return new TaskDTOLazy(toLazy(in.taskGroup(), new ReferenceCycleTracking()), in.name());
	}
}