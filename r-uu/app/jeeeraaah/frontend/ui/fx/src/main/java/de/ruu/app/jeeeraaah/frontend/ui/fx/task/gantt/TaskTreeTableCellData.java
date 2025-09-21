package de.ruu.app.jeeeraaah.frontend.ui.fx.task.gantt;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

import static de.ruu.app.jeeeraaah.frontend.ui.fx.task.gantt.TaskTreeTableCellData.PlanningStatus.ACTIVITY_IN_PERIOD_DEFINED;
import static de.ruu.app.jeeeraaah.frontend.ui.fx.task.gantt.TaskTreeTableCellData.PlanningStatus.ACTIVITY_IN_PERIOD_OPEN_END;
import static de.ruu.app.jeeeraaah.frontend.ui.fx.task.gantt.TaskTreeTableCellData.PlanningStatus.NO_ACTIVITY;

@Getter
@Accessors(fluent = true)
class TaskTreeTableCellData
{
	@RequiredArgsConstructor
	@Getter
	@Accessors(fluent = true)
	enum PlanningStatus
	{
		NO_ACTIVITY                  (""),
		ACTIVITY_IN_PERIOD_DEFINED   ("x"),
		ACTIVITY_IN_PERIOD_OPEN_START("?<x"),
		ACTIVITY_IN_PERIOD_OPEN_END  ("x<?"),
		ACTIVITY_IN_PERIOD_UNDEFINED ("?<x<?"),
		;

		private @NonNull final String symbol;
	}

	private final @NonNull TaskTreeTableDataItem          item;
	private final @NonNull LocalDate                      date;
	private final @NonNull ObjectProperty<PlanningStatus> planningStatus = new ReadOnlyObjectWrapper<>();

	TaskTreeTableCellData(@NonNull TaskTreeTableDataItem item, @NonNull LocalDate date)
	{
		this.item = item;
		this.date = date;
		planningStatus.set(calculatePlanningStatus());
	}

	private PlanningStatus calculatePlanningStatus()
	{
		PlanningStatus result;
		TaskBean task = item.task();

		if (task.start().isPresent())
		{
			// known begin (estimated)
			LocalDate begin = task.start().get().minusDays(1);
			if (task.end().isPresent())
			{
				// begin and end are known so full in period check is possible
				LocalDate end = task.end().get().plusDays(1);
				if (date.isAfter(begin) && date.isBefore(end))
						result = ACTIVITY_IN_PERIOD_DEFINED; // date is  inside period
				else
						result = NO_ACTIVITY;                // date is outside period
			}
			else
			{
				// known begin and unknown end of period means that as long as date is after begin it is unknown if date is
				// inside period
				if (date.isAfter(begin))
						result = ACTIVITY_IN_PERIOD_OPEN_END; // date is  inside period, end is unknown
				else
						result = NO_ACTIVITY;                 // date is outside period
			}
		}
		else
		{
			// unknown begin (estimated)
			if (task.end().isPresent())
			{
				// unknown begin and known end means that as long as date is before end it is unknown if date is inside period
				LocalDate end = task.end().get().plusDays(1);
				if (date.isBefore(end))
						result = PlanningStatus.ACTIVITY_IN_PERIOD_OPEN_START; // date is  inside period, begin is unknown
				else
						result = NO_ACTIVITY;                   // date is outside period
			}
			else
			{
				if (task.superTask().isPresent())
						result = PlanningStatus.ACTIVITY_IN_PERIOD_UNDEFINED; // period is unknown
				else
						result = NO_ACTIVITY;
			}
		}
		return result;
	}
}