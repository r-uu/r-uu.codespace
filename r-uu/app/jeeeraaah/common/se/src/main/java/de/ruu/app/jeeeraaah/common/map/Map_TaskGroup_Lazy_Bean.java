package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.fx.TaskGroupFXBean;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

/** {@link TaskGroupEntityDTO} -> {@link TaskGroupEntityJPA} */
@Mapper public interface Map_TaskGroup_Lazy_Bean
{
	Map_TaskGroup_Lazy_Bean INSTANCE = Mappers.getMapper(Map_TaskGroup_Lazy_Bean.class);

	@NonNull TaskGroupBean map(@NonNull TaskGroupLazy in);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping
	default void beforeMapping(
			@NonNull                TaskGroupLazy in,
			@NonNull @MappingTarget TaskGroupBean out)
	{
		out.beforeMapping(in);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping
	default void afterMapping(
			@NonNull                TaskGroupLazy in,
			@NonNull @MappingTarget TaskGroupBean out)
	{
		out.afterMapping(in);
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * If {@code in} is already persisted, {@link TaskGroupFXBean#entityInfo()} will be propagated to new {@link
	 * TaskGroupBean}.
	 */
	@ObjectFactory default @NonNull TaskGroupBean create(@NonNull TaskGroupLazy in)
	{
		TaskGroupBean result;
		if (in.isPersisted()) result = new TaskGroupBean(in, in.name());
		else                  result = new TaskGroupBean(    in.name());
//		context.put(in, result); // TODO necessary?
		return result;
	}
}