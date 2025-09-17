package de.ruu.app.jeeeraaah.frontend.ui.fx.types;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class Test_Map_Task_EntityDTO_Bean
{
	@Test void standalone()
	{
		String        name = "name";
		TaskEntityDTO task = createTaskEntity(createTaskGroupEntity(), name);

		assertThat(task.name    ()            , is(name ));
		assertThat(task.superTask().isPresent(), is(false));
		assertThat(task.subTasks().isPresent(), is(false));
	}

	@Test void standaloneMapped()
	{
		TaskEntityDTO task   = createTaskEntity(createTaskGroupEntity(), "name");
		TaskBean      mapped = Map_Task_EntityDTO_Bean.INSTANCE.map(task, new ReferenceCycleTracking());

		assertIs(task, mapped);
	}

	@Test void standaloneReMapped()
	{
		ReferenceCycleTracking context = new ReferenceCycleTracking();
		TaskEntityDTO task     = createTaskEntity(createTaskGroupEntity(), "name");
		TaskBean        mapped = Map_Task_EntityDTO_Bean.INSTANCE.map(task, new ReferenceCycleTracking());
		TaskEntityDTO remapped = mapped.toDTO(context);

		assertIs(remapped, mapped);
	}

	@Test void withTasks()
	{
		int    count = 3;

		TaskGroupEntityDTO group = createTaskGroupEntity();
		createTasks(group, 3);

		assertThat(group.tasks().isPresent() , is(true ));
		assertThat(group.tasks().get().size(), is(count));

		ReferenceCycleTracking context = new ReferenceCycleTracking();
		for (TaskEntityDTO dto : group.tasks().get())
		{
			TaskBean        mapped = Map_Task_EntityDTO_Bean.INSTANCE.map(dto, context);
			TaskEntityDTO remapped = mapped.toDTO(context);

			assertIs(dto     , mapped);
			assertIs(remapped, mapped);
		}
	}

	static void assertIs(TaskEntityDTO dto, TaskBean bean)
	{
		assertThat("unexpected id"         , dto.id         (), is(bean.id         ()));
		assertThat("unexpected version"    , dto.version    (), is(bean.version    ()));
		assertThat("unexpected name"       , dto.name       (), is(bean.name       ()));
		assertThat("unexpected description", dto.description(), is(bean.description()));
		assertThat("unexpected closed"     , dto.closed     (), is(bean.closed     ()));

		assertThat("unexpected parent"      , dto.superTask   ().isPresent(), is(bean.superTask   ().isPresent()));
		assertThat("unexpected children"    , dto.subTasks    ().isPresent(), is(bean.subTasks    ().isPresent()));
		assertThat("unexpected predecessors", dto.predecessors().isPresent(), is(bean.predecessors().isPresent()));
		assertThat("unexpected successors"  , dto.successors  ().isPresent(), is(bean.successors  ().isPresent()));

		dto.subTasks    ().ifPresent(ts -> assertThat("unexpected size children"    , ts.size(), is(bean.subTasks    ().get().size())));
		dto.predecessors().ifPresent(ts -> assertThat("unexpected size predecessors", ts.size(), is(bean.predecessors().get().size())));
		dto.successors  ().ifPresent(ts -> assertThat("unexpected size successors"  , ts.size(), is(bean.successors  ().get().size())));

		// assert parent
		if (dto.superTask().isPresent())
		{
			assertThat(bean.superTask().isPresent(), is(true));
			Test_Map_Task_EntityDTO_Bean.assertIs(dto.superTask().get(), bean.superTask().get());
		}
		else { assertThat(bean.superTask().isPresent(), is(false)); }

		// assert children
		if (dto.subTasks().isPresent())
		{
			assertThat(bean.subTasks().isPresent(), is(true));
			for (TaskEntityDTO task : dto.subTasks().get())
			{
				assertThat(bean.subTasks().get().contains(task), is(true));
			}
		}
	}

	private TaskGroupEntityDTO createTaskGroupEntity() { return new TaskGroupEntityDTO("name"); }
	private TaskEntityDTO      createTaskEntity(TaskGroupEntityDTO group, String name)
	{
		TaskEntityDTO result = new TaskEntityDTO(group, name);
		result
				.description("description")
				.start      (LocalDate.now())
				.end        (LocalDate.now())
				.closed     (true)
				;
		return result;
	}

	private void createTask (TaskGroupEntityDTO group, String name) { new TaskEntityDTO(group, name); }
	private void createTasks(TaskGroupEntityDTO group, int count)
	{
		for (int i = 0; i < count; i++) createTask(group, "task " + i);
	}
}