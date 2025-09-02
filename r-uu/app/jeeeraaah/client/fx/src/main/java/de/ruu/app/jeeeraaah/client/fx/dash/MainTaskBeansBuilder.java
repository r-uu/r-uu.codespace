package de.ruu.app.jeeeraaah.client.fx.dash;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.dto.TaskLazy;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.NonNull;

import java.util.Collections;
import java.util.Set;

@ApplicationScoped
class MainTaskBeansBuilder
{
	/**
	 * Convert the given lazy parameters to a set of fully populated task beans. The task group lazy is used to create a
	 * task group bean which is then used to create the task beans from the lazy tasks.
	 *
	 * @param groupLazy task group lazy
	 * @param tasksLazy lazy tasks of the group
	 * @return set of task beans representing the main tasks of the group
	 */
	Set<TaskBean> build(@NonNull TaskGroupLazy groupLazy, @NonNull Set<TaskLazy> tasksLazy)
	{
		TaskGroupBean groupBean = groupLazy.toBean(tasksLazy);
		if (groupBean.mainTasks().isPresent()) return groupBean.mainTasks().get();
		return Collections.emptySet();
	}
}