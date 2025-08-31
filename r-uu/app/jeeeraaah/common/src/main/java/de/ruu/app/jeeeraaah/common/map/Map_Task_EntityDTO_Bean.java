package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
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

/** {@link TaskEntityDTO} -> {@link TaskBean} */
@Mapper public interface Map_Task_EntityDTO_Bean
{
	Map_Task_EntityDTO_Bean INSTANCE = Mappers.getMapper(Map_Task_EntityDTO_Bean.class);

	@Mapping(target = "taskGroup", ignore = true) // ignore task group, because it is mapped in object factory
	@NonNull TaskBean map(@NonNull TaskEntityDTO in, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskEntityDTO          in,
			@NonNull @MappingTarget TaskBean               out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.beforeMapping(in, context);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskEntityDTO          in,
			@NonNull @MappingTarget TaskBean               out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.afterMapping(in, context);
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * if {@code in} is already persisted, {@link Entity#entityInfo()} will be propagated to new {@link TaskBean}
	 */
	@ObjectFactory default @NonNull TaskBean create(
			@NonNull          TaskEntityDTO          in,
			@NonNull @Context ReferenceCycleTracking context)
	{
		TaskGroupBean group = context.get(in.taskGroup(), TaskGroupBean.class);

		if (isNull(group))
		{
			if (in.taskGroup().isPersisted()) group = new TaskGroupBean(in.taskGroup(), in.taskGroup().name());
			else                              group = new TaskGroupBean(                in.taskGroup().name());
			context.put(in.taskGroup(), group);
		}

		TaskBean result = new TaskBean(group, in.name());
//		context.put(in, result); // TODO necessary?

		return result;
	}
}