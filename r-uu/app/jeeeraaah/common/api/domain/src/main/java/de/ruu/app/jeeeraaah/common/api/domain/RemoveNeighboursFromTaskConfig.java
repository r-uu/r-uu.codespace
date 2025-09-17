package de.ruu.app.jeeeraaah.common.api.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Accessors(fluent = true)
@NoArgsConstructor // No-args constructor is needed for Jackson deserialization
public class RemoveNeighboursFromTaskConfig
{
	/** the id of the task to be removed from its neighbours */
	private @NonNull Long idTask;

	@Setter
	private boolean removeFromSuperTask = false;

	private final Set<Long> removeFromPredecessors = new HashSet<>();
	private final Set<Long> removeFromSubTasks     = new HashSet<>();
	private final Set<Long> removeFromSuccessors   = new HashSet<>();

	public RemoveNeighboursFromTaskConfig(
			@NonNull Long idTask,
			         boolean removeFromSuperTask,
			@NonNull Set<Long> removeFromPredecessors,
			@NonNull Set<Long> removeFromSubTasks,
			@NonNull Set<Long> removeFromSuccessors)
	{
		this.idTask = idTask;
		this.removeFromSuperTask = removeFromSuperTask;
		this.removeFromPredecessors.addAll(removeFromPredecessors);
		this.removeFromSubTasks    .addAll(removeFromSubTasks);
		this.removeFromSuccessors  .addAll(removeFromSuccessors);
	}
}