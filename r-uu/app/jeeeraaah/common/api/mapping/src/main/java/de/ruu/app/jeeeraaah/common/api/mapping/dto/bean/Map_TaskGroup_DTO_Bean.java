package de.ruu.app.jeeeraaah.common.api.mapping.dto.bean;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.api.domain.TaskEntity;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupEntity;
import de.ruu.app.jeeeraaah.common.api.mapping.Mappings;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

import java.util.Objects;
import java.util.Set;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/** {@link TaskGroupDTO} -> {@link TaskGroupBean} */
@Mapper public interface Map_TaskGroup_DTO_Bean
{
	Map_TaskGroup_DTO_Bean INSTANCE = Mappers.getMapper(Map_TaskGroup_DTO_Bean.class);

	@NonNull TaskGroupBean map(@NonNull TaskGroupDTO in, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskGroupDTO           in,
			@NonNull @MappingTarget TaskGroupBean          out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		// required arguments are set in constructor
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskGroupDTO           in,
			@NonNull @MappingTarget TaskGroupBean          out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		if (in.tasks().isPresent())
		{
			Set<TaskDTO> relatedTasks = in.tasks().get();
			for (TaskDTO relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task, task will be added to this task group during mapping
						Mappings.toBean(relatedTask, context);
			}
		}
	}

	/** mapstruct object factory */
	@ObjectFactory default @NonNull TaskGroupBean create(@NonNull TaskGroupDTO in)
	{
		return new TaskGroupBean(requireNonNull(in.id()), requireNonNull(in.version()), in.name());
	}
}