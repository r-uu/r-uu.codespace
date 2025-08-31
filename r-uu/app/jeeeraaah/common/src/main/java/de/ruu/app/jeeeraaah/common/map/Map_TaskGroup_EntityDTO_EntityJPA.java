package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

/** {@link TaskGroupEntityDTO} -> {@link TaskGroupEntityJPA} */
@Mapper public interface Map_TaskGroup_EntityDTO_EntityJPA
{
	Map_TaskGroup_EntityDTO_EntityJPA INSTANCE = Mappers.getMapper(Map_TaskGroup_EntityDTO_EntityJPA.class);

	@NonNull TaskGroupEntityJPA map(@NonNull TaskGroupEntityDTO in, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskGroupEntityDTO     in,
			@NonNull @MappingTarget TaskGroupEntityJPA     out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.beforeMapping(in, context);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskGroupEntityDTO     in,
			@NonNull @MappingTarget TaskGroupEntityJPA     out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.afterMapping(in, context);
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * If {@code in} is already persisted, {@link TaskGroupEntityDTO#entityInfo()} will be propagated to new {@link
	 * TaskGroupEntityJPA}.
	 */
	@ObjectFactory default @NonNull TaskGroupEntityJPA create(@NonNull TaskGroupEntityDTO in)
	{
		TaskGroupEntityJPA result;
		if (in.isPersisted()) result = new TaskGroupEntityJPA(in, in.name());
		else                  result = new TaskGroupEntityJPA(    in.name());
//		context.put(in, result); // TODO necessary?
		return result;
	}
}