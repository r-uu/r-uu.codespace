package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskLazy;
import de.ruu.lib.jpa.core.Entity;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

/** {@link TaskEntityDTO} -> {@link TaskBean} */
@Mapper public interface Map_Task_Lazy_Bean
{
	Map_Task_Lazy_Bean INSTANCE = Mappers.getMapper(Map_Task_Lazy_Bean.class);

	@Mapping(target = "taskGroup"  , ignore = true            ) // ignore task group, because it is mapped in object factory
//	@Mapping(target = "name"       , source = "in.name"       ) // select name        from in to be mapped to task bean name
	@Mapping(target = "description", source = "in.description") // select description from in to be mapped to task bean description
//	@Mapping(target = "startDate"  , source = "in.startDate"	) // select startDate   from in to be mapped to task bean startDate
//	@Mapping(target = "endDate"    , source = "in.endDate"    ) // select endDate     from in to be mapped to task bean endDate
//	@Mapping(target = "closed"     , source = "in.closed"     ) // select closed      from in to be mapped to task bean closed

	@NonNull TaskBean map(
			@NonNull TaskGroupBean groupBean,
			@NonNull TaskLazy      in);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskGroupBean groupBean,
			@NonNull                TaskLazy      in,
			@NonNull @MappingTarget TaskBean      out)
	{
		out.beforeMapping(groupBean, in);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskGroupBean groupBean,
			@NonNull                TaskLazy      in,
			@NonNull @MappingTarget TaskBean      out)
	{
		out.afterMapping(groupBean, in);
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * if {@code in} is already persisted, {@link Entity#entityInfo()} will be propagated to new {@link TaskBean}
	 */
	@ObjectFactory default @NonNull TaskBean create(
			@NonNull TaskGroupBean groupBean,
			@NonNull TaskLazy      in)
	{
		return new TaskBean(in, groupBean, in.name());
	}
}