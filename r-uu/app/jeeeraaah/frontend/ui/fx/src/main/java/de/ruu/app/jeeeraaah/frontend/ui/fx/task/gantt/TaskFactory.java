package de.ruu.app.jeeeraaah.frontend.ui.fx.task.gantt;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static de.ruu.lib.util.BooleanFunctions.not;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
class TaskFactory
{
	private final @NonNull LocalDate start;
	private final @NonNull LocalDate end;

	@NonNull Set<TaskBean> rootTasks()
	{
		Set<TaskBean> result = new HashSet<>();

		TaskGroupBean group1 = new TaskGroupBean("group 1");
//		TaskGroupBean taskGroup2 = new TaskGroupBean("group 2");
//		TaskGroupBean taskGroup3 = new TaskGroupBean("group 3");

//		result.add(rootTaskWithSubTasksForGroupAtDate(LocalDate.of(2025, 1,  1), group1, 1));
//		result.add(rootTaskWithSubTasksForGroupAtDate(LocalDate.of(2025, 1, 11), group1, 2));
//		result.add(rootTaskWithSubTasksForGroupAtDate(LocalDate.of(2025, 1, 21), group1, 3));
//		result.add(rootTaskWithSubTasksForGroupAtDate(LocalDate.of(2025, 1,  1), taskGroup2, 1));
//		result.add(rootTaskWithSubTasksForGroupAtDate(LocalDate.of(2025, 1, 11), taskGroup2, 2));
//		result.add(rootTaskWithSubTasksForGroupAtDate(LocalDate.of(2025, 1, 21), taskGroup2, 3));

//		result.add(rootTaskWithoutStart(start, group1));
//		result.add(rootTaskWithoutFinish(LocalDate.of(2025, 1, 1), taskGroup3));

//		result.add(rootTaskWithNeitherStartNorFinish(taskGroup3));

		TaskBean root  = new TaskBean(group1, "root 1-31");
		root.start(start);
		root.end(end);

		TaskBean child;

		child = new TaskBean(group1, "child 1: 1-3");
		child.start(start);
		child.end(start.plusDays(2));
		root.addSubTask(child);

		child = new TaskBean(group1, "child 2: 4-6");
		child.start(start.plusDays(3));
		child.end(start.plusDays(5));
		root.addSubTask(child);

		child = new TaskBean(group1, "child 3:  -6");
		child.end(start.plusDays(5));
		root.addSubTask(child);

		child = new TaskBean(group1, "child 4: 6-");
		child.start(start.plusDays(5));
		root.addSubTask(child);

		result.add(root);

		return result;
	}

	private @NonNull TaskBean rootTaskWithSubTasksForGroupAtDate(
			@NonNull LocalDate date, @NonNull TaskGroupBean taskGroup, int i)
	{
		// root task has no parent, no predecessors and no successors
		TaskBean result = new TaskBean(taskGroup, "root task " + taskGroup.name() + "." + i);
		result.start(date);
		result.end(date.plusDays(9));

		// child rootTasks
		TaskBean predecessor = null;
		for (int j = 0; j < 3; j++)
		{
			TaskBean childTask =
					new TaskBean(taskGroup, date.format(DateTimeFormatter.BASIC_ISO_DATE) + ", task " + i + "." + j);
			childTask.superTask(result);
			childTask.start(date.plusDays(j));
			childTask.end(date.plusDays(j + 1));
			if (not(isNull(predecessor))) childTask.addPredecessor(predecessor);
			predecessor = childTask;
		}
		return result;
	}

	private @NonNull TaskBean rootTaskWithoutStart(@NonNull LocalDate date, @NonNull TaskGroupBean taskGroup)
	{
		TaskBean result = new TaskBean(taskGroup, "root no start");
		TaskBean child  = new TaskBean(taskGroup, "task no start");
		result.start(date);
		result.end(date);
		child .end(date);
		result.addSubTask(child);
		return result;
	}

	private @NonNull TaskBean rootTaskWithoutFinish(@NonNull LocalDate date, @NonNull TaskGroupBean taskGroup)
	{
		TaskBean result = new TaskBean(taskGroup, "root no finish");
		TaskBean child  = new TaskBean(taskGroup, "task no finish");
		result.start(date);
		result.end(date);
		child.start(date);
		result.addSubTask(child);
		return result;
	}

	private @NonNull TaskBean rootTaskWithNeitherStartNorFinish(@NonNull TaskGroupBean taskGroup)
	{
		TaskBean result = new TaskBean(taskGroup, "root no start no finish");
		TaskBean child  = new TaskBean(taskGroup, "task no start no finish");
		result.start(start);
		result.end(end);
		child .start(start);
		result.addSubTask(child);
		return result;
	}
}