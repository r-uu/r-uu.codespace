package de.ruu.app.jeeeraaah.frontend.ui.fx.task.gantt;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.lib.util.Time;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link TaskTreeTableDataItem} represents an item in a tree table view for {@link TaskBean}s (see {@link #task}). As
 * tree table view items instances of this class provide cell data objects (see {@link #cells}) for each day in the
 * period spanned from {@code startOfPeriod} and {@code endOfPeriod} in {@link TaskTreeTableDataItem}.
 */
@Slf4j
@Getter
@Accessors(fluent = true)
class TaskTreeTableDataItem
{
	private final @NonNull TaskBean                    task;
	private final @NonNull List<TaskTreeTableCellData> cells = new ArrayList<>();

	public TaskTreeTableDataItem(@NonNull TaskBean task, @NonNull LocalDate startOfPeriod, @NonNull LocalDate endOfPeriod)
	{
		this.task = task;
		for (LocalDate date : Time.datesInPeriod(startOfPeriod, endOfPeriod.plusDays(1)))
		{
			cells.add(new TaskTreeTableCellData(this, date));
		}
		log.debug("task {} has {} cell data objects", task.name(), cells.size());
	}
}