package de.ruu.app.jeeeraaah.common.api.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class TaskComparatorTest
{
    @Test
    void sortsByName_and_handlesNullsFirst()
    {
        TestTypes.SimpleTaskGroup g = new TestTypes.SimpleTaskGroup("g");
        TestTypes.SimpleTask a = new TestTypes.SimpleTask(g, "Alpha");
        TestTypes.SimpleTask b = new TestTypes.SimpleTask(g, "Beta");
        TestTypes.SimpleTask c = new TestTypes.SimpleTask(g, "Charlie");

        List<Task> list = new ArrayList<>(Arrays.asList(b, null, c, a, null));
        Collections.sort(list, Task.COMPARATOR);

        // Expect nulls first, then alphabetical by name
        assertThat(list.get(0), is(nullValue()));
        assertThat(list.get(1), is(nullValue()));
        assertThat(((Task<?, ?>) list.get(2)).name(), is("Alpha"));
        assertThat(((Task<?, ?>) list.get(3)).name(), is("Beta"));
        assertThat(((Task<?, ?>) list.get(4)).name(), is("Charlie"));
    }
}
