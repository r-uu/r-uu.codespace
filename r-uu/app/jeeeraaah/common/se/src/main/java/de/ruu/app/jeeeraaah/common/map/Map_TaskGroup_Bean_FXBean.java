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

/** {@link TaskGroupBean} -> {@link TaskGroupFXBean} */
@Mapper
public interface Map_TaskGroup_Bean_FXBean
{
	Map_TaskGroup_Bean_FXBean INSTANCE = Mappers.getMapper(Map_TaskGroup_Bean_FXBean.class);

	@NonNull TaskGroupFXBean map(@NonNull TaskGroupBean input, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskGroupBean          in,
			@NonNull @MappingTarget TaskGroupFXBean        out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.beforeMapping(in, context);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskGroupBean          in,
			@NonNull @MappingTarget TaskGroupFXBean        out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.afterMapping(in, context);
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * If {@code in} is already persisted, {@link TaskGroupBean#entityInfo()} will be propagated to new {@link
	 * TaskGroupFXBean}.
	 */
	@ObjectFactory default @NonNull TaskGroupFXBean create(@NonNull TaskGroupBean in)
	{
		TaskGroupFXBean result;
		if (in.isPersisted()) result = new TaskGroupFXBean(in, in.name());
		else                  result = new TaskGroupFXBean(    in.name());
//		context.put(in, result); // TODO necessary?
		return result;
	}
}