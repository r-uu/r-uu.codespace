package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.fx.TaskGroupFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

/** {@link TaskGroupFXBean} -> {@link TaskGroupBean} */
@Mapper public interface Map_TaskGroup_FXBean_Bean
{
	Map_TaskGroup_FXBean_Bean INSTANCE = Mappers.getMapper(Map_TaskGroup_FXBean_Bean.class);

	@NonNull TaskGroupBean map(@NonNull TaskGroupFXBean input, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskGroupFXBean        in,
			@NonNull @MappingTarget TaskGroupBean          out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.beforeMapping(in, context);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskGroupFXBean        in,
			@NonNull @MappingTarget TaskGroupBean          out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.afterMapping(in, context);
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * If {@code in} is already persisted, {@link TaskGroupFXBean#entityInfo()} will be propagated to new {@link
	 * TaskGroupBean}.
	 */
	@ObjectFactory @NonNull default TaskGroupBean create(@NonNull TaskGroupFXBean in)
	{
		TaskGroupBean result;
		if (in.isPersisted()) result = new TaskGroupBean(in, in.name());
		else                  result = new TaskGroupBean(    in.name());
//		context.put(in, result); // TODO necessary?
		return result;
	}
}