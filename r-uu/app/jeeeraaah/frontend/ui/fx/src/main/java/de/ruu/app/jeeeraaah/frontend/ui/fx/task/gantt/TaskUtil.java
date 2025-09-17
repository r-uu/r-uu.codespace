package de.ruu.app.jeeeraaah.frontend.ui.fx.task.gantt;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import lombok.NonNull;

import java.util.Comparator;

abstract class TaskUtil
{
	static class StartEstimatedComparator implements Comparator<TaskBean>
	{
		@Override public int compare(@NonNull TaskBean t1, @NonNull TaskBean t2)
		{
			if (t1.start().isPresent() && t2.start().isPresent())
					return t1.start().get().compareTo(t2.start().get());
			return 0;
		}
	}
	final static StartEstimatedComparator START_ESTIMATED_COMPARATOR = new StartEstimatedComparator();
}