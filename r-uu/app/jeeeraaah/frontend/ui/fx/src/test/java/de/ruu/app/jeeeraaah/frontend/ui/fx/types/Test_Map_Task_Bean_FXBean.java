package de.ruu.app.jeeeraaah.frontend.ui.fx.types;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.fx.TaskFXBean;
import de.ruu.app.jeeeraaah.common.fx.TaskGroupFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import lombok.NonNull;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class Test_Map_Task_Bean_FXBean
{
	@Test void standalone()
	{
		String   name = "name";
		TaskBean task = createTaskBean(createTaskGroupBean(), name);

		assertThat(task.name  (), is(name));

		assertThat(task.closed      ()            , is(false));
		assertThat(task.description ().isPresent(), is(false));
		assertThat(task.superTask   ().isPresent(), is(false));
		assertThat(task.subTasks    ().isPresent(), is(false));
		assertThat(task.predecessors().isPresent(), is(false));
		assertThat(task.successors  ().isPresent(), is(false));
	}

	@Test void standaloneMapped()
	{
		ReferenceCycleTracking context = new ReferenceCycleTracking();

		TaskBean   task   = createTaskBean(createTaskGroupBean(), "name");
		TaskFXBean mapped = task.toFXBean(context);

		assertIs(task, mapped);
	}

	@Test void standaloneReMapped()
	{
		ReferenceCycleTracking context = new ReferenceCycleTracking();

		TaskBean       task = createTaskBean(createTaskGroupBean(), "name");
		TaskFXBean   mapped = task  .toFXBean(context);
		TaskBean   reMapped = mapped.toBean  (context);

		assertIs(reMapped, mapped);
	}

	@Test void withTasks()
	{
		int    count = 3;

		TaskGroupBean group = createTaskGroupBean();
		createTasks(group, 3);

		assertThat(group.tasks().isPresent() , is(true ));
		assertThat(group.tasks().get().size(), is(count));

		ReferenceCycleTracking context = new ReferenceCycleTracking();

		for (TaskBean bean : group.tasks().get())
		{
			TaskFXBean   mapped = bean.toFXBean(context);
			TaskBean   remapped = mapped.toBean(context);

			assertIs(bean     , mapped);
			assertIs(remapped, mapped);
		}
	}

	private void assertIs(TaskBean taskBean, TaskFXBean taskFXBean)
	{
		assertThat("unexpected name"       , taskFXBean.name       (), is(taskBean.name       ()));
		assertThat("unexpected description", taskFXBean.description(), is(taskBean.description()));
		assertThat("unexpected start"      , taskFXBean.start      (), is(taskBean.start      ()));
		assertThat("unexpected end"        , taskFXBean.end        (), is(taskBean.end        ()));
		assertThat("unexpected closed"     , taskFXBean.closed     (), is(taskBean.closed     ()));

		assertIs(taskFXBean.taskGroup(), taskBean.taskGroup());

		assertThat("unexpected parent"      , taskFXBean.superTask().isPresent(), is(taskBean.superTask().isPresent()));
		assertThat("unexpected children"    , taskFXBean.subTasks().isPresent(), is(taskBean.subTasks().isPresent()));
		assertThat("unexpected predecessors", taskFXBean.predecessors().isPresent(), is(taskBean.predecessors().isPresent()));
		assertThat("unexpected successors"  , taskFXBean.successors  ().isPresent(), is(taskBean.successors  ().isPresent()));

		taskFXBean.subTasks().ifPresent(ts -> assertThat("unexpected children size"    , ts.size(), is(taskBean.subTasks().get().size())));
		taskFXBean.predecessors().ifPresent(ts -> assertThat("unexpected predecessors size", ts.size(), is(taskBean.predecessors().get().size())));
		taskFXBean.successors  ().ifPresent(ts -> assertThat("unexpected successors size"  , ts.size(), is(taskBean.successors  ().get().size())));
	}

	private void assertIs(@NonNull TaskGroupFXBean fxBean, @NonNull TaskGroupBean bean)
	{
		assertThat(fxBean.id         (), is(bean.id         ()));
		assertThat(fxBean.version    (), is(bean.version    ()));
		assertThat(fxBean.name       (), is(bean.name       ()));
		assertThat(fxBean.description(), is(bean.description()));
	}

	private TaskGroupBean createTaskGroupBean() { return new TaskGroupBean("name"); }
	private TaskBean createTaskBean(TaskGroupBean group, String name) { return new TaskBean(group, name ); }
	private void createTasks(TaskGroupBean group, int count)
	{
		for (int i = 0; i < count; i++) createTaskBean(group, "task " + i);
	}
}