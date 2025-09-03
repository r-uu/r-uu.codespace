package de.ruu.app.jeeeraaah.common.bean;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static de.ruu.lib.util.BooleanFunctions.not;

@RequiredArgsConstructor
@Slf4j
public class PreconditionCheckRelationalOperations
{
	private final @NonNull TaskBean  task;
	private                Collector collector; // lazy initialisation by collector()

	public boolean canBeAddedAsSubTask(@NonNull TaskBean otherTask)
	{
		if (task == otherTask)
		{
//			log.warn("{} can not be sub task of itself", task);
			return false;
		}
		if (not(task.taskGroup().equals(otherTask.taskGroup())))
		{
//			log.warn("task of foreign group {} can not be sub task of task in group {}", otherTask.taskGroup(), task.taskGroup());
			return false;
		}
		if (collector().superTasks().contains(otherTask))
		{
//			log.warn("{} is (indirect) super task of {} and cannot be sub task at the same time", otherTask, task);
			return false;
		}
		if (collector().subTasks().contains(otherTask))
		{
//			log.warn("{} already is (indirect) sub task of {}", otherTask, task);
			return false;
		}
		if (collector().predecessors().contains(otherTask))
		{
//			log.warn("{} can not be (indirect) predecessor and sub task of {} at the same time", otherTask, task);
			return false;
		}
		if (collector().successors().contains(otherTask))
		{
//			log.warn("{} can not be (indirect) successor and sub task of {} at the same time", otherTask, task);
			return false;
		}
		return true;
	}

	public boolean canBeAddedAsPredecessor(@NonNull TaskBean otherTask)
	{
		if (task == otherTask)
		{
//			log.warn("{} can not be predecessor of itself", task);
			return false;
		}
		if (not(task.taskGroup().equals(otherTask.taskGroup())))
		{
//			log.warn("task of foreign group {} can not be predecessor of task in group {}", otherTask.taskGroup(), task.taskGroup());
			return false;
		}
		if (collector().superTasks().contains(otherTask))
		{
//			log.warn("{} is (indirect) parent of {} and cannot be predecessor at the same time", otherTask, task);
			return false;
		}
		if (collector().subTasks().contains(otherTask))
		{
//			log.warn("{} is (indirect) child of {} and cannot be predecessor at the same time", otherTask, task);
			return false;
		}
		if (collector().predecessors().contains(otherTask))
		{
//			log.warn("{} already is (indirect) predecessor of {}", otherTask, task);
			return false;
		}
		if (collector().successors().contains(otherTask))
		{
//			log.warn("{} can not be successor and predecessor of {} at the same time", otherTask, task);
			return false;
		}
		return true;
	}

	public boolean canBeAddedAsSuccessor(@NonNull TaskBean otherTask)
	{
		if (task == otherTask)
		{
//			log.warn("{} can not be successor of itself", task);
			return false;
		}
		if (not(task.taskGroup().equals(otherTask.taskGroup())))
		{
//			log.warn("task of foreign group {} can not be successor of task in group {}", otherTask.taskGroup(), task.taskGroup());
			return false;
		}
		if (collector().superTasks().contains(otherTask))
		{
//			log.warn("{} is (indirect) parent of {} and cannot be successor at the same time", otherTask, task);
			return false;
		}
		if (collector().subTasks().contains(otherTask))
		{
//			log.warn("{} is (indirect) child of {} and cannot be successor at the same time", otherTask, task);
			return false;
		}
		if (collector().predecessors().contains(otherTask))
		{
//			log.warn("{} can not be successor and predecessor of {} at the same time", otherTask, task);			return false;
		}
		if (collector().successors().contains(otherTask))
		{
//			log.warn("{} already is (indirect) successor of {}", otherTask, task);
			return false;
		}
		return true;
	}

	private Collector collector()
	{
		if (collector == null) collector = new Collector(task);
		return collector;
	}
}