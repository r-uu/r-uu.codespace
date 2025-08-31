package de.ruu.app.jeeeraaah.common;

import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static de.ruu.lib.util.BooleanFunctions.not;

@AllArgsConstructor
@Slf4j
public class DataFactory
{
	private int groupCount;
	private int mainTasksPerGroupCount;
	private int subTasksPerMainTaskCount;

	public Set<TaskGroupEntityDTO> data()
	{
		Set<TaskGroupEntityDTO> result = new HashSet<>();
		for (int idGroup = 0; idGroup < groupCount; idGroup++) { result.add(createGroup(idGroup)); }
		return result;
	}

	private TaskGroupEntityDTO createGroup(int idGroup)
	{
		TaskGroupEntityDTO result = new TaskGroupEntityDTO("" + idGroup);
		for (int idMainTask = 0; idMainTask < mainTasksPerGroupCount; idMainTask++)
		{
			createMainTask(result, idMainTask);
		}
		return result;
	}

	private TaskEntityDTO createMainTask(TaskGroupEntityDTO group, int idMainTask)
	{
		TaskEntityDTO result = new TaskEntityDTO(group, group.name() + "." + idMainTask);
		for (int idSubTask = 0; idSubTask < subTasksPerMainTaskCount; idSubTask++)
		{
			createSubTaskBean(result, idSubTask);
		}
		return result;
	}

	private TaskEntityDTO createSubTaskBean(TaskEntityDTO superTask, int idSubTask)
	{
		TaskEntityDTO result = new TaskEntityDTO(superTask.taskGroup(), superTask.name() + "." + idSubTask);
		superTask.addSubTask(result);
		return result;
	}

	private void createPredecessorsAndSuccessorsAmongChildren(Set<TaskEntityDTO> children)
	{
		TaskEntityDTO recentPredecessor = null;
		for (TaskEntityDTO child : children)
		{
			if (not(recentPredecessor == null)) recentPredecessor.addSuccessor(child);
			recentPredecessor = child;
		}
	}

	public static void main(String[] args)
	{
		Set<TaskGroupEntityDTO> data = new DataFactory(1, 1, 1).data();
		for (TaskGroupEntityDTO taskGroup : data)
		{
			log.debug("group {}", taskGroup.name());
			Optional<Set<TaskEntityDTO>> optional = taskGroup.mainTaskEntityDTOs();
			if (optional.isPresent())
			{
				for (TaskEntityDTO mainTask : optional.get())
				{
					log.debug("main task {}", mainTask.name());
					optional = mainTask.subTasks();
					if (optional.isPresent())
					{
						for (TaskEntityDTO subTask : optional.get())
						{
							log.debug("subTask {}", subTask.name());
						}
					}
				}
			}
		}
//		TaskGroupEntityDTO group = new TaskGroupEntityDTO("group");
//		TaskEntityDTO      task  = new TaskEntityDTO(group, "task");
//
//		Optional<Set<TaskEntityDTO>> optionalTasks = group.tasks();
//		optionalTasks
//				.ifPresentOrElse
//				(
//						ts -> log.debug("tasks in group: " + ts.size()),
//						() -> log.debug("tasks in group: 0")
//				);
//		;
//		Set<TaskEntityDTO> set = new HashSet<>();
//		set.add(task);
//		set.add(task);
//		log.debug("tasks in set: " + set.size());
	}
}