package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

/** {@link TaskGroupBean} -> {@link TaskGroupEntityDTO} */
@Mapper
public interface Map_TaskGroup_Bean_EntityDTO
{
	Map_TaskGroup_Bean_EntityDTO INSTANCE = Mappers.getMapper(Map_TaskGroup_Bean_EntityDTO.class);

	@NonNull TaskGroupEntityDTO map(@NonNull TaskGroupBean in, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskGroupBean          in,
			@NonNull @MappingTarget TaskGroupEntityDTO     out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.beforeMapping(in, context);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskGroupBean          in,
			@NonNull @MappingTarget TaskGroupEntityDTO     out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.afterMapping(in, context);
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * If {@code in} is already persisted, {@link TaskGroupEntityDTO#entityInfo()} will be propagated to new {@link
	 * TaskGroupEntityDTO}.
	 */
	@ObjectFactory default @NonNull TaskGroupEntityDTO create(@NonNull TaskGroupBean in)
	{
		TaskGroupEntityDTO result;
		if (in.isPersisted()) result = new TaskGroupEntityDTO(in, in.name());
		else                  result = new TaskGroupEntityDTO(    in.name());
//		context.put(in, result); // TODO necessary?
		return result;
	}
}