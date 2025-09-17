package de.ruu.app.jeeeraaah.common.api.domain;

import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Accessors(fluent = true)
@ToString
public class TaskCreationData
{
	@NonNull private Long          taskGroupId;
	@NonNull private TaskLazy      task;
}