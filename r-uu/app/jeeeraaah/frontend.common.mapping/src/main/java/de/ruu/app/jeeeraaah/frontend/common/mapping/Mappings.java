package de.ruu.app.jeeeraaah.frontend.common.mapping;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.frontend.common.mapping.bean.fxbean.Map_TaskGroup_Bean_FXBean;
import de.ruu.app.jeeeraaah.frontend.common.mapping.bean.fxbean.Map_Task_Bean_FXBean;
import de.ruu.app.jeeeraaah.frontend.common.mapping.fxbean.bean.Map_TaskGroup_FXBean_Bean;
import de.ruu.app.jeeeraaah.frontend.common.mapping.fxbean.bean.Map_Task_FXBean_Bean;
import de.ruu.app.jeeeraaah.frontend.ui.fx.TaskFXBean;
import de.ruu.app.jeeeraaah.frontend.ui.fx.TaskGroupFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;

public interface Mappings
{
	static TaskGroupBean   toBean(TaskGroupFXBean in, ReferenceCycleTracking context)
	{
		return Map_TaskGroup_FXBean_Bean.INSTANCE.map(in, context);
	}
	static TaskBean        toBean(TaskFXBean in, ReferenceCycleTracking context)
	{
		return Map_Task_FXBean_Bean     .INSTANCE.map(in, context);
	}
	static TaskGroupFXBean toFXBean(TaskGroupBean in, ReferenceCycleTracking context)
	{
		return Map_TaskGroup_Bean_FXBean.INSTANCE.map(in, context);
	}
	static TaskFXBean      toFXBean(TaskBean      in, ReferenceCycleTracking context)
	{
		return Map_Task_Bean_FXBean     .INSTANCE.map(in, context);
	}
}
