package de.ruu.app.jeeeraaah.common.api.mapping;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.api.mapping.bean.dto.Map_TaskGroup_Bean_DTO;
import de.ruu.app.jeeeraaah.common.api.mapping.bean.dto.Map_Task_Bean_DTO;
import de.ruu.app.jeeeraaah.common.api.mapping.dto.bean.Map_TaskGroup_DTO_Bean;
import de.ruu.app.jeeeraaah.common.api.mapping.dto.bean.Map_Task_DTO_Bean;
import de.ruu.app.jeeeraaah.common.api.mapping.lazy.bean.Map_TaskGroup_Lazy_Bean;
import de.ruu.app.jeeeraaah.common.api.mapping.lazy.bean.Map_Task_Lazy_Bean;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTOLazy;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTOLazy;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import lombok.NonNull;

public interface Mappings
{
	static @NonNull TaskGroupDTO  toDTO(@NonNull TaskGroupBean     in, @NonNull ReferenceCycleTracking context)
	{
		return Map_TaskGroup_Bean_DTO .INSTANCE.map(in, context);
	}
	static @NonNull TaskDTO       toDTO(@NonNull TaskBean          in, @NonNull ReferenceCycleTracking context)
	{
		return Map_Task_Bean_DTO      .INSTANCE.map(in, context);
	}
	static @NonNull TaskGroupBean toBean(@NonNull TaskGroupDTO     in, @NonNull ReferenceCycleTracking context)
	{
		return Map_TaskGroup_DTO_Bean .INSTANCE.map(in, context);
	}
	static @NonNull TaskBean      toBean(@NonNull TaskDTO          in, @NonNull ReferenceCycleTracking context)
	{
		return Map_Task_DTO_Bean      .INSTANCE.map(in, context);
	}
	static @NonNull TaskGroupBean toBean(@NonNull TaskGroupDTOLazy in, @NonNull ReferenceCycleTracking context)
	{
		return Map_TaskGroup_Lazy_Bean.INSTANCE.map(in);
	}
	static @NonNull TaskBean      toBean(
			            @NonNull TaskGroupBean group, TaskDTOLazy in,@NonNull ReferenceCycleTracking context)
	{
		return Map_Task_Lazy_Bean     .INSTANCE.map(group, in);
	}
}