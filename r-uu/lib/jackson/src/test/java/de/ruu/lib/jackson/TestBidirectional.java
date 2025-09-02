package de.ruu.lib.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@Slf4j
class TestBidirectional
{
    private static ObjectMapper MAPPER;

    @BeforeAll
    static void beforeAll() { MAPPER = new JacksonContextResolver().mapper(); }

    @Test void taskGroupSingle() throws JsonProcessingException
    {
        TaskGroup groupIn  = new TaskGroup("group");
        String    jsonIn   = MAPPER.writeValueAsString(groupIn);
        log.debug("jsonIn\n{}", jsonIn);

        TaskGroup groupOut = MAPPER.readValue(jsonIn, TaskGroup.class);
        String    jsonOut  = MAPPER.writeValueAsString(groupOut);
        log.debug("jsonOut\n{}", jsonOut);
        assertThat("jsonOut is not jsonIn", jsonIn, is(jsonOut));

        assertThat("groupOut is not groupIn", groupIn, is(groupOut));
    }

    @Test void taskGroupSingleWithTaskSingle() throws JsonProcessingException
    {
        TaskGroup groupIn = new TaskGroup("group");
        Task      taskIn  = new Task(groupIn, "task");
        String    jsonIn  = MAPPER.writeValueAsString(groupIn);
        log.debug("jsonIn\n{}", jsonIn);

        TaskGroup groupOut = MAPPER.readValue(jsonIn, TaskGroup.class);
        String    jsonOut  = MAPPER.writeValueAsString(groupOut);
        log.debug("jsonOut\n{}", jsonOut);
        assertThat("jsonOut is not jsonIn"    , jsonIn                 , is(jsonOut));

        assertThat("groupOut is not groupIn"  , groupIn                , is(groupOut));
        assertThat("groupOut.tasks is null"   , groupOut.tasks()       , is(notNullValue()));
        assertThat("groupOut.tasks.size not 1", groupOut.tasks().size(), is(1));

        Task taskOut = groupOut.tasks().iterator().next();

        assertThat("taskOut is not taskIn"            , taskOut        , is(taskIn));
        assertThat("taskOut.group is not taskIn.group", taskOut.group(), is(taskIn.group()));
    }

    @Test void taskWithAllRelationsSingle() throws JsonProcessingException
    {
        TaskGroup groupIn           = new TaskGroup(         "group");
        Task      taskIn            = new Task     (groupIn, "task");
        Task      taskSuperIn       = new Task     (groupIn, "super task");
        Task      taskSubIn         = new Task     (groupIn, "sub task");
        Task      taskPredecessorIn = new Task     (groupIn, "predecessor task");
        Task      taskSuccessorIn   = new Task     (groupIn, "successor task");

        taskIn.superTask     (taskSuperIn);
        taskIn.addSubTask    (taskSubIn);
        taskIn.addPredecessor(taskPredecessorIn);
        taskIn.addSuccessor  (taskSuccessorIn);

        String    jsonIn  = MAPPER.writeValueAsString(groupIn);
        log.debug("jsonIn\n{}", jsonIn);

        TaskGroup groupOut = MAPPER.readValue(jsonIn, TaskGroup.class);
        String    jsonOut  = MAPPER.writeValueAsString(groupOut);
        log.debug("jsonOut\n{}", jsonOut);

        assertThat("jsonOut is not jsonIn"    , jsonIn                 , is(jsonOut)); // sequence of elements may vary
        assertThat("groupOut is not groupIn"  , groupIn                , is(groupOut));
        assertThat("groupOut.tasks is null"   , groupOut.tasks()       , is(notNullValue()));
        assertThat("groupOut.tasks.size not 1", groupOut.tasks().size(), is(5));

        Optional<Task> optional = groupOut.tasks().stream().filter(t -> t.equals(taskIn)).findFirst();
        assertThat("no task out matching task in", optional.isPresent(), is(true));

        Task taskOut = optional.get();
        assertThat("taskOut is not taskIn"            , taskOut        , is(taskIn));
        assertThat("taskOut.group is not taskIn.group", taskOut.group(), is(taskSuperIn.group()));

        Task taskSuperOut = taskOut.superTask();
        assertThat("taskSuperOut is null"                       , taskSuperOut                  , is(notNullValue()));
        assertThat("taskSuperOut.group is not taskSuperIn.group", taskSuperOut.group()          , is(taskSuperIn.group()));
        assertThat("taskSuperOut is taskSuperIn"                , taskSuperOut                  , is(taskSuperIn));
        assertThat("taskSuperOut.subTasks is not null"          , taskSuperOut.subTasks()       , is(notNullValue()));
        assertThat("taskSuperOut.subTasks.size is not 1"         , taskSuperOut.subTasks().size(), is(1));

        Task taskSubOut = taskOut.subTasks().iterator().next();
        assertThat("taskSubOut is null"                     , taskSubOut           , is(notNullValue()));
        assertThat("taskSubOut.group is not taskSubIn.group", taskSubOut.group()   , is(taskSubIn.group()));
        assertThat("taskSubOut is taskSubIn"                , taskSubOut           , is(taskSubIn));
        assertThat("taskSubOut.subTasks is null"            , taskSubOut.subTasks(), is(nullValue()));

        Task taskPredecessorOut = taskOut.predecessors().iterator().next();
        assertThat("taskPredecessorOut is null"                             , taskPredecessorOut                    , is(notNullValue()));
        assertThat("taskPredecessorOut.group is not taskPredecessorIn.group", taskPredecessorOut.group()            , is(taskPredecessorIn.group()));
        assertThat("taskPredecessorOut is taskPredecessorIn"                , taskPredecessorOut                    , is(taskPredecessorIn));
        assertThat("taskPredecessorOut.subTasks is null"                    , taskPredecessorOut.subTasks()         , is(nullValue()));
        assertThat("taskPredecessorOut.successors is not null"              , taskPredecessorOut.successors()       , is(notNullValue()));
        assertThat("taskPredecessorOut.successors.size is not 1"            , taskPredecessorOut.successors().size(), is(1));

        Task taskSuccessorOut = taskOut.successors().iterator().next();
        assertThat("taskSuccessorOut is null"                           , taskSuccessorOut                      , is(notNullValue()));
        assertThat("taskSuccessorOut.group is not taskSuccessorIn.group", taskSuccessorOut.group()              , is(taskSuccessorOut.group()));
        assertThat("taskSuccessorOut is taskSuccessorIn"                , taskSuccessorOut                      , is(taskSuccessorOut));
        assertThat("taskSuccessorOut.subTasks is null"                  , taskSuccessorOut.subTasks()           , is(nullValue()));
        assertThat("taskSuccessorOut.predecessors is not null"          , taskSuccessorOut.predecessors()       , is(notNullValue()));
        assertThat("taskSuccessorOut.predecessors.size is not 1"        , taskSuccessorOut.predecessors().size(), is(1));
    }
}