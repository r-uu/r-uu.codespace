package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA;
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

/** {@link TaskEntityJPA} -> {@link TaskEntityDTO} */
@Mapper public interface Map_Task_EntityJPA_EntityDTO
{
	Map_Task_EntityJPA_EntityDTO INSTANCE = Mappers.getMapper(Map_Task_EntityJPA_EntityDTO.class);

	@Mapping(target = "taskGroup", ignore = true) // ignore task group, because it is mapped in object factory
	@NonNull TaskEntityDTO map(@NonNull TaskEntityJPA in, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskEntityJPA          in,
			@NonNull @MappingTarget TaskEntityDTO          out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.beforeMapping(in, context);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskEntityJPA          in,
			@NonNull @MappingTarget TaskEntityDTO          out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		out.afterMapping(in, context);
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * if {@code in} is already persisted, {@link Entity#entityInfo()} will be propagated to new {@link TaskEntityDTO}
	 */
	@ObjectFactory @NonNull default TaskEntityDTO create(
			@NonNull          TaskEntityJPA          in,
			@NonNull @Context ReferenceCycleTracking context)
	{
		TaskGroupEntityDTO group = context.get(in.taskGroup(), TaskGroupEntityDTO.class);

		if (isNull(group))
		{
			if (in.taskGroup().isPersisted()) group = new TaskGroupEntityDTO(in.taskGroup(), in.taskGroup().name());
			else                              group = new TaskGroupEntityDTO(                in.taskGroup().name());
			context.put(in.taskGroup(), group);
		}

		TaskEntityDTO result = new TaskEntityDTO(group, in.name());

		return result;
	}
}