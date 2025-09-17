package de.ruu.app.jeeeraaah.frontend.common.mapping.fxbean.bean;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.api.mapping.Mappings;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskBean;
import de.ruu.app.jeeeraaah.common.fx.TaskFXBean;
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

/** {@link TaskBean} -> {@link TaskBean} */
@Mapper public interface Map_Task_FXBean_Bean
{
	Map_Task_FXBean_Bean INSTANCE = Mappers.getMapper(Map_Task_FXBean_Bean.class);

	@Mapping(target = "taskGroup", ignore = true) // ignore task group, because it is mapped in object factory
	@NonNull TaskBean map(@NonNull TaskFXBean in, @NonNull @Context ReferenceCycleTracking context);

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@BeforeMapping default void beforeMapping(
			@NonNull                TaskBean in,
			@NonNull @MappingTarget TaskBean out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		// required arguments id, version and name are already set in constructor
	}

	/** annotating parameter {@code out} with {@link MappingTarget} is essential for this method being called */
	@AfterMapping default void afterMapping(
			@NonNull                TaskBean     in,
			@NonNull @MappingTarget TaskBean     out,
			@NonNull @Context       ReferenceCycleTracking context)
	{
		if (in.superTask().isPresent())
		{
			TaskBean relatedTask       = in.superTask().get();
			// check if related task was already mapped
			TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
			if (isNull(relatedTaskMapped))
					// start new mapping for related task
					out.superTask(Mappings.toBean(relatedTask, context));
			else
					// use already mapped related task
					out.superTask(relatedTaskMapped);
		}
		if (in.subTasks().isPresent())
		{
			Set<TaskBean> relatedTasks = in.subTasks().get();
			for (TaskBean relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						out.addSubTask(Mappings.toBean(relatedTask, context));
				else
						// use already mapped related task
						out.addSubTask(relatedTaskMapped);
			}
		}
		if (in.predecessors().isPresent())
		{
			Set<TaskBean> relatedTasks = in.predecessors().get();
			for (TaskBean relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						out.addPredecessor(Mappings.toBean(relatedTask, context));
				else
						// use already mapped related task
						out.addPredecessor(relatedTaskMapped);
			}
		}
		if (in.successors().isPresent())
		{
			Set<TaskBean> relatedTasks = in.successors().get();
			for (TaskBean relatedTask : relatedTasks)
			{
				// check if related task was already mapped
				TaskBean relatedTaskMapped = context.get(relatedTask, TaskBean.class);
				if (isNull(relatedTaskMapped))
						// start new mapping for related task
						out.addSuccessor(Mappings.toBean(relatedTask, context));
				else
						// use already mapped related task
						out.addSuccessor(relatedTaskMapped);
			}
		}
	}

	/**
	 * mapstruct object factory
	 * <p>
	 * if {@code in} is already persisted, {@link Entity#entityInfo()} will be propagated to new {@link TaskBean}
	 */
	@ObjectFactory default @NonNull TaskBean create(
			@NonNull          TaskBean                in,
			@NonNull @Context ReferenceCycleTracking context)
	{
		TaskGroupBean group = context.get(in.taskGroup(), TaskGroupBean.class);

		if (isNull(group))
		{
			group = new TaskGroupBean(in.taskGroup());
			context.put(in.taskGroup(), group);
		}

		return new TaskBean(group, in.name());
	}
}