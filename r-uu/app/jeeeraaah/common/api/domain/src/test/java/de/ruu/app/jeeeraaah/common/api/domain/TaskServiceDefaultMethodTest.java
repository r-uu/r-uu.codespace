package de.ruu.app.jeeeraaah.common.api.domain;

import lombok.NonNull;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TaskServiceDefaultMethodTest
{
    static class DummyTaskService implements TaskService<TestTypes.SimpleTask>
    {
        final AtomicReference<Long> lastReadId = new AtomicReference<>();
        final Optional<TestTypes.SimpleTask> toReturn;

        DummyTaskService(Optional<TestTypes.SimpleTask> toReturn) { this.toReturn = toReturn; }

        @Override public @NonNull TestTypes.SimpleTask create(@NonNull TestTypes.SimpleTask task) { throw new UnsupportedOperationException(); }
        @Override public Optional<? extends TestTypes.SimpleTask> read(@NonNull Long id) { lastReadId.set(id); return toReturn; }
        @Override public @NonNull TestTypes.SimpleTask update(@NonNull TestTypes.SimpleTask task) { throw new UnsupportedOperationException(); }
        @Override public void delete(@NonNull Long id) { throw new UnsupportedOperationException(); }
        @Override public Set<? extends TestTypes.SimpleTask> findAll() { throw new UnsupportedOperationException(); }
        @Override public Optional<? extends TestTypes.SimpleTask> findWithRelated(@NonNull Long id) { throw new UnsupportedOperationException(); }
        @Override public void addSubTask(@NonNull TestTypes.SimpleTask task, @NonNull TestTypes.SimpleTask subTask) { throw new UnsupportedOperationException(); }
        @Override public void addPredecessor(@NonNull TestTypes.SimpleTask task, @NonNull TestTypes.SimpleTask predecessor) { throw new UnsupportedOperationException(); }
        @Override public void addSuccessor(@NonNull TestTypes.SimpleTask task, @NonNull TestTypes.SimpleTask successor) { throw new UnsupportedOperationException(); }
        @Override public void removeSuperSubTask(@NonNull TestTypes.SimpleTask task, @NonNull TestTypes.SimpleTask subTask) { throw new UnsupportedOperationException(); }
        @Override public void removePredecessor(@NonNull TestTypes.SimpleTask task, @NonNull TestTypes.SimpleTask predecessor) { throw new UnsupportedOperationException(); }
        @Override public void removeSuccessor(@NonNull TestTypes.SimpleTask task, @NonNull TestTypes.SimpleTask successor) { throw new UnsupportedOperationException(); }
        @Override public void removeNeighboursFromTask(@NonNull RemoveNeighboursFromTaskConfig removeNeighboursFromTaskConfig) { throw new UnsupportedOperationException(); }
    }

    @Test
    void defaultFindDelegatesToRead()
            throws Exception
    {
        TestTypes.SimpleTaskGroup g = new TestTypes.SimpleTaskGroup("g");
        TestTypes.SimpleTask t = new TestTypes.SimpleTask(g, "X");
        DummyTaskService svc = new DummyTaskService(Optional.of(t));

        Optional<? extends TestTypes.SimpleTask> result = svc.find(123L);

        assertThat(svc.lastReadId.get(), is(123L));
        assertThat(result.isPresent(), is(true));
        assertThat(result.get().name(), is("X"));
    }
}
