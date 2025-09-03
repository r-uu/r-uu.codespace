package de.ruu.app.jeeeraaah.common.bean;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
class Collector
{
	@NonNull private final TaskBean taskBean;

	Set<TaskBean> superTasks()
	{
		Set<TaskBean> result = new HashSet<>();
		if (taskBean.superTask().isPresent())
		{
			TaskBean task = taskBean.superTask().get();
			result.add(task);
			result.addAll(new Collector(task).superTasks());
		}
		return result;
	}

	Set<TaskBean> subTasks()
	{
		Set<TaskBean> result = new HashSet<>();
		if (taskBean.subTasks().isPresent())
		{
			for (TaskBean task : taskBean.subTasks().get())
			{
				result.add(task);
				result.addAll(new Collector(task).subTasks());
			}
		}
		return result;
	}

	Set<TaskBean> predecessors()
	{
		Set<TaskBean> result = new HashSet<>();
		if (taskBean.predecessors().isPresent())
		{
			for (TaskBean task : taskBean.predecessors().get())
			{
				result.add(task);
				result.addAll(new Collector(task).predecessors());
			}
		}
		return result;
	}

	Set<TaskBean> successors()
	{
		Set<TaskBean> result = new HashSet<>();
		if (taskBean.successors().isPresent())
		{
			for (TaskBean task : taskBean.successors().get())
			{
				result.add(task);
				result.addAll(new Collector(task).successors());
			}
		}
		return result;
	}
}