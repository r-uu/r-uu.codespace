package de.ruu.app.jeeeraaah.common;

import jakarta.ws.rs.NotFoundException;
import lombok.NonNull;

import java.util.Optional;
import java.util.Set;

public interface TaskService<T extends Task<? extends TaskGroup<?>, ? extends Task<?, ?>>>
{
	@NonNull           T  create(@NonNull T    task);
	Optional<? extends T> read  (@NonNull Long id  );
	@NonNull           T  update(@NonNull T    task);
	void                  delete(@NonNull Long id  );

	Set     <? extends T> findAll();
	Optional<? extends T> findWithRelated(@NonNull Long id);

	default Optional<? extends T> find(@NonNull Long id) { return read(id); }

	void addSubTask    (@NonNull T task, @NonNull T subTask    ) throws NotFoundException;
	void addPredecessor(@NonNull T task, @NonNull T predecessor) throws NotFoundException;
	void addSuccessor  (@NonNull T task, @NonNull T successor  ) throws NotFoundException;

	void removeSuperSubTask(@NonNull T task, @NonNull T subTask    ) throws NotFoundException;
	void removePredecessor (@NonNull T task, @NonNull T predecessor) throws NotFoundException;
	void removeSuccessor   (@NonNull T task, @NonNull T successor  ) throws NotFoundException;

	boolean removeNeighboursFromTask(@NonNull RemoveNeighboursFromTaskConfig removeNeighboursFromTaskConfig);
}