package de.ruu.app.jeeeraaah.frontend.ui.fx.types;

import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class Test_Map_TaskGroup_DTO_Bean
{
	@Test void standalone()
	{
		String             name  = "name";
		TaskGroupEntityDTO group = createGroup(name);

		assertThat(group.name()             , is(name));
		assertThat(group.tasks().isPresent(), is(false));
	}

	@Test void standaloneMapped()
	{
		ReferenceCycleTracking context = new ReferenceCycleTracking();

		TaskGroupEntityDTO group  = createGroup("name");
		TaskGroupBean      mapped = group.toBean(context);

		assertIs(mapped, group);
	}

	@Test void standaloneReMapped()
	{
		ReferenceCycleTracking context  = new ReferenceCycleTracking();
		TaskGroupEntityDTO     group    = createGroup("name");
		TaskGroupBean            mapped = group .toBean(context);
		TaskGroupEntityDTO     reMapped = mapped.toDTO(context);

		assertIs(mapped, reMapped);
	}

	@Test void withSubTasks()
	{
		String name  = "name";
		int    count = 3;

		TaskGroupEntityDTO group = createGroup(name);
		createTasks(group, 3);

		assertThat(group.tasks().isPresent() , is(true));
		assertThat(group.tasks().get().size(), is(count));
	}

	void assertIs(TaskGroupBean bean, TaskGroupEntityDTO dto)
	{
		assertThat(bean.id               (), is(dto.id               ()));
		assertThat(bean.version          (), is(dto.version          ()));
		assertThat(bean.name             (), is(dto.name             ()));
		assertThat(bean.description      (), is(dto.description      ()));
		assertThat(bean.tasks().isPresent(), is(dto.tasks().isPresent()));

		bean.tasks().ifPresent(ts -> assertThat(ts.size(), is(dto.tasks().get().size())));
	}

	private TaskGroupEntityDTO createGroup(                          String name) { return new TaskGroupEntityDTO("name"); }
	private void               createTask (TaskGroupEntityDTO group, String name) { new TaskEntityDTO(group, name); }
	private void               createTasks(TaskGroupEntityDTO group, int count)
	{
		for (int i = 0; i < count; i++) createTask(group, "task " + i);
	}
}