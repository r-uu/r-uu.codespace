package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.fx.TaskFXBean;
import de.ruu.app.jeeeraaah.common.fx.TaskGroupFXBean;
import de.ruu.lib.jpa.core.Entity;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

import static java.util.Objects.isNull;

/** {@link TaskBean} -> {@link TaskFXBean} */
@Mapper public interface Map_Task_Bean_FXBean
{
	Map_Task_Bean_FXBean INSTANCE = Mappers.getMapper(Map_Task_Bean_FXBean.class);

	@Mapping(target = "taskGroup", ignore = true) // ignore task group, because it is mapped in object factory
	@NonNull TaskFXBean map(@NonNull TaskBean input, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskBean               in,
			@NonNull @MappingTarget TaskFXBean             out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.beforeMapping(in, context);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskBean               in,
			@NonNull @MappingTarget TaskFXBean             out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.afterMapping(in, context);
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * if {@code in} is already persisted, {@link Entity#entityInfo()} will be propagated to new {@link TaskFXBean}
	 */
	@ObjectFactory default @NonNull TaskFXBean create(
			@NonNull          TaskBean               in,
			@NonNull @Context ReferenceCycleTracking context)
	{
		TaskGroupFXBean group = context.get(in.taskGroup(), TaskGroupFXBean.class);

		if (isNull(group))
		{
			if (in.taskGroup().isPersisted()) group = new TaskGroupFXBean(in.taskGroup(), in.taskGroup().name());
			else                              group = new TaskGroupFXBean(                in.taskGroup().name());
			context.put(in.taskGroup(), group);
		}

		TaskFXBean result = new TaskFXBean(group, in.name());
//		context.put(in, result); // TODO necessary?

		return result;
	}
}