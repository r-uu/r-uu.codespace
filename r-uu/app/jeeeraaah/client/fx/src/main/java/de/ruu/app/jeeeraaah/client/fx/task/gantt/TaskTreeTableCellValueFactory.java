package de.ruu.app.jeeeraaah.client.fx.task.gantt;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.fx.TaskFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.util.Callback;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
public class TaskTreeTableCellValueFactory
		implements Callback<CellDataFeatures<TaskFXBean, String>, ObservableValue<String>>
{
	private final @NonNull LocalDate date;

	@Override public StringProperty call(CellDataFeatures<TaskFXBean, String> cdf)
	{
		ReferenceCycleTracking context = new ReferenceCycleTracking();
		TaskFXBean taskBean = cdf.getValue().getValue();
//		log.debug("cell value factory for {}", taskBean.name());
		return readOnlyStringWrapperFor(taskBean.toBean(context));
	}

	private ReadOnlyStringWrapper readOnlyStringWrapperFor(TaskBean taskBean)
	{
//		return new ReadOnlyStringWrapper("bla");
		if (taskBean.start().isPresent())
		{
			// known begin
			LocalDate begin = taskBean.start().get().minusDays(1);
			if (taskBean.end().isPresent())
			{
				// begin and end are known so full in period check is possible
				LocalDate end = taskBean.end().get().plusDays(1);
				if (date.isAfter(begin) && date.isBefore(end))
					// date is inside period
					return new ReadOnlyStringWrapper("x");
				// date is outside period
				return new ReadOnlyStringWrapper("");
			}
			// known begin and unknown end of period means that as long as date is after begin it is unknown if date is
			// inside period
			if (date.isAfter(begin))
				// end is unknown
				return new ReadOnlyStringWrapper("<?");
			// date is outside period
			return new ReadOnlyStringWrapper("");
		}
		else
		{
			// unknown begin
			if (taskBean.end().isPresent())
			{
				// unknown begin and known end means that as long as date is before end it is unknown if date is inside period
				LocalDate end = taskBean.end().get().plusDays(1);
				if (date.isBefore(end))
					// begin is unknown
					return new ReadOnlyStringWrapper("?<");
				// date is outside period
				return new ReadOnlyStringWrapper("");
			}
			if (taskBean.superTask().isPresent())
				// period is unknown
				return new ReadOnlyStringWrapper("<?<");
			return new ReadOnlyStringWrapper("");
		}
	}
}