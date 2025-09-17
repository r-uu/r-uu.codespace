package de.ruu.app.jeeeraaah.backend.common.mapping.dto.jpa;

import de.ruu.app.jeeeraaah.backend.common.mapping.Mappings;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
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

import java.util.Set;

import static java.util.Objects.isNull;

/** {@link TaskDTO} -> {@link TaskJPA} */
@Mapper public interface Map_Task_DTO_JPA
{
	Map_Task_DTO_JPA INSTANCE = Mappers.getMapper(Map_Task_DTO_JPA.class);

	@Mapping(target = "taskGroup", ignore = true) // ignore task group, because it is mapped in object factory
	@NonNull TaskJPA map(@NonNull TaskDTO in, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskDTO in,
			@NonNull @MappingTarget TaskJPA out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		// required arguments id, version and name are already set in constructor
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskDTO     in,
			@NonNull @MappingTarget TaskJPA     out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		if (in.superTask().isPresent())
		{
			TaskDTO relatedTask       = in.superTask().get();
			// check if related task was already mapped
			TaskJPA relatedTaskMapped = context.get(relatedTask, TaskJPA.class);
			if (isNull(relatedTaskMapped))
					// start new mapping for related task
					out.superTask(Mappings.toJPA(relatedTask, context));
			else
					// use already mapped related task
					out.superTask(relatedTaskMapped);
		}
		if (in.subTasks().isPresent())
		{
			Set<TaskDTO> relatedTasks = in.subTasks().get();
			for (TaskDTO relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskJPA relatedTaskMapped = context.get(relatedTask, TaskJPA.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						out.addSubTask(Mappings.toJPA(relatedTask, context));
				else
						// use already mapped related task
						out.addSubTask(relatedTaskMapped);
			}
		}
		if (in.predecessors().isPresent())
		{
			Set<TaskDTO> relatedTasks = in.predecessors().get();
			for (TaskDTO relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskJPA relatedTaskMapped = context.get(relatedTask, TaskJPA.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						out.addPredecessor(Mappings.toJPA(relatedTask, context));
				else
						// use already mapped related task
						out.addPredecessor(relatedTaskMapped);
			}
		}
		if (in.successors().isPresent())
		{
			Set<TaskDTO> relatedTasks = in.successors().get();
			for (TaskDTO relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskJPA relatedTaskMapped = context.get(relatedTask, TaskJPA.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						out.addSuccessor(Mappings.toJPA(relatedTask, context));
				else
						// use already mapped related task
						out.addSuccessor(relatedTaskMapped);
			}
		}
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * if {@code in} is already persisted, {@link Entity#entityInfo()} will be propagated to new {@link TaskJPA}
	 */
	@ObjectFactory default @NonNull TaskJPA create(
			@NonNull          TaskDTO          in,
			@NonNull @Context ReferenceCycleTracking context)
	{
		TaskGroupJPA group = context.get(in.taskGroup(), TaskGroupJPA.class);

		if (isNull(group))
		{
			group = new TaskGroupJPA(in.taskGroup());
			context.put(in.taskGroup(), group);
		}

		TaskJPA result = new TaskJPA(group, in.name());

		return result;
	}
}