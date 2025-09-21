package de.ruu.app.jeeeraaah.frontend.ui.fx.types;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static de.ruu.lib.util.BooleanFunctions.not;

@Slf4j
public class DataAsStringTransformer
{
	String asString(Set<TaskGroupBean> taskGroups)
	{
		List<String> result = new ArrayList<>();
		taskGroups.forEach(tg -> result.add(asString(tg)));
		Collections.sort(result);
		return String.join("\n", result);
	}

	private String asString(TaskGroupBean taskGroup)
	{
		List<String> result = new ArrayList<>();
		if (taskGroup.tasks().isPresent())
		{
			Optional<Set<TaskBean>> optional = findMainTasks(taskGroup.tasks().get());
			optional.ifPresent(mainTasks -> mainTasks.forEach(t -> result.add(asStringMainTask(t))));
		}
		Collections.sort(result);
		return taskGroup.name() + "\n\t" + String.join("\n\t", result);
	}

	private String asStringMainTask(TaskBean task)
	{
		List<String> result = new ArrayList<>();
		if (task.subTasks().isPresent())
		{
			task.subTasks().get().forEach(s -> result.add(asStringSubTask((TaskBean) s)));
		}
		Collections.sort(result);
		return task.name() + "\n\t\t" + String.join("\n\t\t", result);
	}

	private String asStringSubTask(TaskBean task) { return task.name(); }

	private Optional<Set<TaskBean>> findMainTasks(Set<TaskBean> tasks)
	{
		return Optional.of(tasks.stream().filter(t -> not(t.superTask().isPresent())).collect(Collectors.toSet()));
	}

	public static void main(String[] args)
	{
		log.debug("\n{}", new DataAsStringTransformer().asString(new DataFactory(3, 3, 3).data()));
	}
}