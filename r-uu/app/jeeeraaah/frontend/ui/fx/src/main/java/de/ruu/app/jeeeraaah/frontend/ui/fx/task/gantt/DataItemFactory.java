package de.ruu.app.jeeeraaah.frontend.ui.fx.task.gantt;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Getter
@Accessors(fluent = true)
class DataItemFactory
{
	private final @NonNull LocalDate startOfPeriod;
	private final @NonNull LocalDate endOfPeriod;

	private final @NonNull List<TaskTreeTableDataItem> rootItemsInPeriod = new ArrayList<>();

	DataItemFactory(@NonNull LocalDate startOfPeriod, @NonNull LocalDate endOfPeriod)
	{
		this.startOfPeriod = startOfPeriod;
		this.endOfPeriod   = endOfPeriod;

		TaskFactory taskFactory = new TaskFactory(startOfPeriod, endOfPeriod);
		Set<TaskBean> rootTasks = taskFactory.rootTasks();

		LocalDate comparePeriodStart = startOfPeriod.minusDays(1);
		LocalDate comparePeriodEnd   = endOfPeriod  .plusDays (1);

		for (TaskBean rootTask : rootTasks)
		{
			if (rootTask.start().isPresent() && rootTask.end().isPresent())
			{
				if (comparePeriodStart.isBefore(rootTask.start().get()) &&
				    comparePeriodEnd  .isAfter (rootTask.end().get()))
				{
					rootItemsInPeriod.add(new TaskTreeTableDataItem(rootTask, startOfPeriod, endOfPeriod));
				}
			}
		}

		rootItemsInPeriod.sort
		(
				(i1, i2) ->
				{
					Optional<LocalDate> optionalStartEstimated1 = i1.task().start();
					Optional<LocalDate> optionalStartEstimated2 = i2.task().start();
					if (optionalStartEstimated1.isPresent() && optionalStartEstimated2.isPresent())
							return
									optionalStartEstimated1.get().compareTo(
									optionalStartEstimated2.get());
					else if (optionalStartEstimated1.isEmpty() && optionalStartEstimated2.isEmpty())
					{
						Optional<LocalDate> optionalEndEstimated1 = i1.task().end();
						Optional<LocalDate> optionalEndEstimated2 = i2.task().end();
						if (optionalEndEstimated1.isPresent() && optionalEndEstimated2.isPresent())
								return
										optionalEndEstimated1.get().compareTo(
										optionalEndEstimated2.get());
					}
					return 0;
				}
		);
	}
}