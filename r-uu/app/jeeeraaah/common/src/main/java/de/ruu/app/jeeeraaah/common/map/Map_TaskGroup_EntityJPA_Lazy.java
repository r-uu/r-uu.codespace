package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/** {@link TaskGroupEntityJPA} -> {@link TaskGroupEntityDTO} */
@Mapper public interface Map_TaskGroup_EntityJPA_Lazy
{
	Map_TaskGroup_EntityJPA_Lazy INSTANCE = Mappers.getMapper(Map_TaskGroup_EntityJPA_Lazy.class);

	@NonNull TaskGroupLazy map(@NonNull TaskGroupEntityJPA in);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskGroupEntityJPA in,
			@NonNull @MappingTarget TaskGroupLazy      out)
	{
		out.beforeMapping(in);
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskGroupEntityJPA in,
			@NonNull @MappingTarget TaskGroupLazy      out)
	{
		out.afterMapping(in);
	}
}