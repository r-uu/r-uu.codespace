package de.ruu.app.jeeeraaah.backend.common.mapping.jpa.dto;

import de.ruu.app.jeeeraaah.backend.common.mapping.Mappings;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
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

import java.util.Set;

import static java.util.Objects.isNull;

/** {@link TaskJPA} -> {@link TaskDTO} */
@Mapper public interface Map_Task_JPA_DTO
{
	Map_Task_JPA_DTO INSTANCE = Mappers.getMapper(Map_Task_JPA_DTO.class);

	@Mapping(target = "taskGroup", ignore = true) // ignore task group, because it is mapped in object factory
	@NonNull
	TaskDTO map(@NonNull TaskJPA in, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskJPA                in,
			@NonNull @MappingTarget TaskDTO                out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		// required arguments id, version and name are already set in constructor
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskJPA                in,
			@NonNull @MappingTarget TaskDTO                out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		if (in.superTask().isPresent())
		{
			TaskJPA relatedTask       = in.superTask().get();
			// check if related task was already mapped
			TaskDTO relatedTaskMapped = context.get(relatedTask, TaskDTO.class);
			if (isNull(relatedTaskMapped))
					// start new mapping for related task
					out.superTask(Mappings.toDTO(relatedTask, context));
			else
					// use already mapped related task
					out.superTask(relatedTaskMapped);
		}
		if (in.subTasks().isPresent())
		{
			Set<TaskJPA> relatedTasks = in.subTasks().get();
			for (TaskJPA relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskDTO relatedTaskMapped = context.get(relatedTask, TaskDTO.class);
				if (isNull(relatedTaskMapped))
				{
						// start new mapping for related task
						out.addSubTask(Mappings.toDTO(relatedTask, context));
				}
				else
						// use already mapped related task
						out.addSubTask(relatedTaskMapped);
			}
		}
		if (in.predecessors().isPresent())
		{
			Set<TaskJPA> relatedTasks = in.predecessors().get();
			for (TaskJPA relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskDTO relatedTaskMapped = context.get(relatedTask, TaskDTO.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						out.addPredecessor(Mappings.toDTO(relatedTask, context));
				else
						// use already mapped related task
						out.addPredecessor(relatedTaskMapped);
			}
		}
		if (in.successors().isPresent())
		{
			Set<TaskJPA> relatedTasks = in.successors().get();
			for (TaskJPA relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskDTO relatedTaskMapped = context.get(relatedTask, TaskDTO.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						out.addSuccessor(Mappings.toDTO(relatedTask, context));
				else
						// use already mapped related task
						out.addSuccessor(relatedTaskMapped);
			}
		}
	}

	/**
	 * mapstruct object factory
	 */
	@ObjectFactory @NonNull default TaskDTO create(
			@NonNull          TaskJPA                in,
			@NonNull @Context ReferenceCycleTracking context)
	{
		TaskGroupDTO group = context.get(in.taskGroup(), TaskGroupDTO.class);

		if (isNull(group))
		{
			group = new TaskGroupDTO(in.taskGroup());
			context.put(in.taskGroup(), group);
		}

		TaskDTO result = new TaskDTO(group, in);

		return result;
	}
}