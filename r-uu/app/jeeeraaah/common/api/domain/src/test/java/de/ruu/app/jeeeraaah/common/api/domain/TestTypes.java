package de.ruu.app.jeeeraaah.common.api.domain;

import jakarta.annotation.Nullable;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.*;

/**
 * Test helper types providing minimal concrete implementations of the domain interfaces
 * to be used by unit tests in this module.
 */
final class TestTypes
{
    private TestTypes() {}

    public static final class SimpleTaskGroup implements TaskGroup<SimpleTask>
    {
        private String name;
        @Nullable private String description;
        @Nullable private Set<SimpleTask> tasks;

        public SimpleTaskGroup(String name) { this.name = name; }

        @Override public @NonNull String name() { return name; }
        @Override public @NonNull TaskGroup<SimpleTask> name(@NonNull String name) { this.name = name; return this; }
        @Override public Optional<String> description() { return Optional.ofNullable(description); }
        @Override public @NonNull TaskGroup<SimpleTask> description(@Nullable String description) { this.description = description; return this; }
        @Override public Optional<Set<SimpleTask>> tasks() { return Optional.ofNullable(tasks == null ? null : Collections.unmodifiableSet(tasks)); }
        @Override public boolean removeTask(@NonNull SimpleTask task) { return tasks != null && tasks.remove(task); }

        /* test helpers */
        public SimpleTaskGroup add(SimpleTask t)
        {
            if (tasks == null) tasks = new HashSet<>();
            tasks.add(t);
            return this;
        }
    }

    public static final class SimpleTask implements Task<SimpleTaskGroup, SimpleTask>
    {
        private final SimpleTaskGroup group;
        private String name;
        private boolean closed = false;
        @Nullable private String description;
        @Nullable private LocalDate start;
        @Nullable private LocalDate end;

        public SimpleTask(SimpleTaskGroup group, String name)
        {
            this.group = Objects.requireNonNull(group);
            this.name  = Objects.requireNonNull(name);
        }

        @Override public @NonNull SimpleTaskGroup taskGroup() { return group; }
        @Override public @NonNull String name() { return name; }
        @Override public @NonNull Boolean closed() { return closed; }
        @Override public Optional<String> description() { return Optional.ofNullable(description); }
        @Override public Optional<LocalDate> start() { return Optional.ofNullable(start); }
        @Override public Optional<LocalDate> end() { return Optional.ofNullable(end); }

        @Override public @NonNull SimpleTask taskGroup(@NonNull SimpleTaskGroup taskGroup) { throw new UnsupportedOperationException("immutable group in tests"); }
        @Override public @NonNull SimpleTask description(String description) { this.description = description; return this; }
        @Override public @NonNull SimpleTask start(LocalDate startEstimated) { this.start = startEstimated; return this; }
        @Override public @NonNull SimpleTask end(LocalDate finishEstimated) { this.end = finishEstimated; return this; }
        @Override public @NonNull SimpleTask closed(Boolean closed) { this.closed = Boolean.TRUE.equals(closed); return this; }

        @Override public Optional<SimpleTask> superTask() { return Optional.empty(); }
        @Override public @NonNull SimpleTask superTask(SimpleTask superTask) { throw new UnsupportedOperationException("not needed for tests"); }
        @Override public Optional<Set<SimpleTask>> subTasks() { return Optional.empty(); }
        @Override public boolean addSubTask(@NonNull SimpleTask task) { return false; }
        @Override public boolean removeSubTask(@NonNull SimpleTask task) { return false; }
        @Override public Optional<Set<SimpleTask>> predecessors() { return Optional.empty(); }
        @Override public boolean addPredecessor(@NonNull SimpleTask task) { return false; }
        @Override public boolean removePredecessor(@NonNull SimpleTask task) { return false; }
        @Override public Optional<Set<SimpleTask>> successors() { return Optional.empty(); }
        @Override public boolean addSuccessor(@NonNull SimpleTask task) { return false; }
        @Override public boolean removeSuccessor(@NonNull SimpleTask task) { return false; }
    }
}
