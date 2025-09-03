package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA;
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

/** {@link TaskEntityDTO} -> {@link TaskEntityJPA} */
@Mapper public interface Map_Task_EntityDTO_EntityJPA
{
	Map_Task_EntityDTO_EntityJPA INSTANCE = Mappers.getMapper(Map_Task_EntityDTO_EntityJPA.class);

	@Mapping(target = "taskGroup", ignore = true) // ignore task group, because it is mapped in object factory
	@NonNull TaskEntityJPA map(@NonNull TaskEntityDTO in, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskEntityDTO in,
			@NonNull @MappingTarget TaskEntityJPA out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.beforeMapping(in, context);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskEntityDTO     in,
			@NonNull @MappingTarget TaskEntityJPA     out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.afterMapping(in, context);
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * if {@code in} is already persisted, {@link Entity#entityInfo()} will be propagated to new {@link TaskEntityJPA}
	 */
	@ObjectFactory default @NonNull TaskEntityJPA create(
			@NonNull          TaskEntityDTO          in,
			@NonNull @Context ReferenceCycleTracking context)
	{
		TaskGroupEntityJPA group = context.get(in.taskGroup(), TaskGroupEntityJPA.class);

		if (isNull(group))
		{
			if (in.taskGroup().isPersisted()) group = new TaskGroupEntityJPA(in.taskGroup(), in.taskGroup().name());
			else                              group = new TaskGroupEntityJPA(                in.taskGroup().name());
			context.put(in.taskGroup(), group);
		}

		TaskEntityJPA result = new TaskEntityJPA(group, in.name());
//		context.put(in, result); // TODO necessary?

		return result;
	}
}