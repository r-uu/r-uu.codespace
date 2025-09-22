package de.ruu.app.jeeeraaah.common.api.mapping.bean.lazy;

import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTOLazy;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

import static java.util.Objects.requireNonNull;

/** {@link TaskGroupDTOLazy} -> {@link TaskGroupBean} */
@Mapper public interface Map_TaskGroup_Bean_Lazy
{
	Map_TaskGroup_Bean_Lazy INSTANCE = Mappers.getMapper(Map_TaskGroup_Bean_Lazy.class);

	@NonNull TaskGroupLazy map(@NonNull TaskGroupBean in);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(@NonNull TaskGroupBean in, @NonNull @MappingTarget TaskGroupLazy out)
	{
		// required arguments are set in constructor
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(@NonNull TaskGroupBean in, @NonNull @MappingTarget TaskGroupLazy out)
	{
		// no manual mappings in addition to those done by mapstruct
	}

	/** mapstruct object factory */
	@ObjectFactory default @NonNull TaskGroupLazy create(@NonNull TaskGroupBean in) { return new TaskGroupDTOLazy(in); }
}