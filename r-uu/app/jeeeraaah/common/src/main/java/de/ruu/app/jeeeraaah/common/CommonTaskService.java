package de.ruu.app.jeeeraaah.common;

import lombok.NonNull;

import java.util.Optional;
import java.util.Set;

public interface CommonTaskService<T extends Task<? extends TaskGroup<?>, ? extends Task<?, ?>>>
{
	Optional<? extends T> create(@NonNull T    task);
	Optional<? extends T> read  (@NonNull Long id  );
	Optional<? extends T> update(@NonNull T    task);
	boolean               delete(@NonNull Long id  );

	Set     <? extends T> findAll();
	Optional<? extends T> findWithRelated(@NonNull Long id);

	default Optional<? extends T> find(@NonNull Long id) { return read(id); }

	void addSubTask    (@NonNull T task, @NonNull T subTask    );
	void addPredecessor(@NonNull T task, @NonNull T predecessor);
	void addSuccessor  (@NonNull T task, @NonNull T successor  );

	void removeSuperSubTask(@NonNull T task, @NonNull T subTask    );
	void removePredecessor (@NonNull T task, @NonNull T predecessor);
	void removeSuccessor   (@NonNull T task, @NonNull T successor  );

	boolean removeNeighboursFromTask(@NonNull RemoveNeighboursFromTaskConfig removeNeighboursFromTaskConfig);
}