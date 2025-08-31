package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.dto.TaskLazy;
import de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA;
import de.ruu.lib.jpa.core.Entity;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

import static java.util.Objects.isNull;

/** {@link TaskEntityJPA} -> {@link TaskEntityDTO} */
@Mapper public interface Map_Task_EntityJPA_Lazy
{
	Map_Task_EntityJPA_Lazy INSTANCE = Mappers.getMapper(Map_Task_EntityJPA_Lazy.class);

	@NonNull TaskLazy map(@NonNull TaskEntityJPA in);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskEntityJPA in,
			@NonNull @MappingTarget TaskLazy      out)
	{
		out.beforeMapping(in);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskEntityJPA in,
			@NonNull @MappingTarget TaskLazy      out)
	{
		out.afterMapping(in);
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * if {@code in} is already persisted, {@link Entity#entityInfo()} will be propagated to new {@link TaskLazy}
	 */
	@ObjectFactory
	default @NonNull TaskLazy create(
			@NonNull          TaskEntityJPA          in,
			@NonNull @Context ReferenceCycleTracking context)
	{
		TaskGroupLazy group = context.get(in.taskGroup(), TaskGroupLazy.class);

		if (isNull(group))
		{
			if (in.taskGroup().isPersisted()) group = new TaskGroupLazy(in.taskGroup(), in.taskGroup().name());
			else                              group = new TaskGroupLazy(                in.taskGroup().name());
			context.put(in.taskGroup(), group);
		}

		TaskLazy result = new TaskLazy(group, in.name());
//		context.put(in, result); // TODO necessary?

		return result;
	}
}