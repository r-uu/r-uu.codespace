package de.ruu.app.jeeeraaah.common.api.mapping.bean.dto;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import lombok.NonNull;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ObjectFactory;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static java.util.Objects.isNull;

/** {@link TaskGroupBean} -> {@link TaskGroupDTO} */
@Mapper
public interface Map_TaskGroup_Bean_DTO
{
	Map_TaskGroup_Bean_DTO INSTANCE = Mappers.getMapper(Map_TaskGroup_Bean_DTO.class);

	@NonNull TaskGroupDTO map(@NonNull TaskGroupBean in, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskGroupBean     in,
			@NonNull @MappingTarget TaskGroupDTO     out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		// required arguments id, version and name are already set in constructor
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskGroupBean     in,
			@NonNull @MappingTarget TaskGroupDTO     out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		if (in.tasks().isPresent())
		{
			Set<TaskBean> relatedTasks = in.tasks().get();
			for (TaskBean relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskDTO relatedTaskMapped = context.get(relatedTask, TaskDTO.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						Map_Task_Bean_DTO.INSTANCE.map(relatedTask, context);
			}
		}
	}

	/** mapstruct object factory */
	@ObjectFactory default @NonNull TaskGroupDTO create(@NonNull TaskGroupBean in) { return new TaskGroupDTO(in); }
}