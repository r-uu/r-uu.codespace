package de.ruu.app.jeeeraaah.frontend.ui.fx.types;

import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import org.junit.jupiter.api.Test;

import static de.ruu.app.jeeeraaah.common.api.mapping.Mappings.toBean;
import static de.ruu.app.jeeeraaah.common.api.mapping.Mappings.toDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class Test_Map_TaskGroup_DTO_Bean
{
	@Test void standalone()
	{
		String       name  = "name";
		TaskGroupDTO group = createGroup(name);

		assertThat(group.name()             , is(name));
		assertThat(group.tasks().isPresent(), is(false));
	}

	@Test void standaloneMapped()
	{
		ReferenceCycleTracking context = new ReferenceCycleTracking();

		TaskGroupDTO  group  = createGroup("name");
		TaskGroupBean mapped = toBean(group,context);

		assertIs(mapped, group);
	}

	@Test void standaloneReMapped()
	{
		ReferenceCycleTracking context  = new ReferenceCycleTracking();

		TaskGroupDTO     group    = createGroup("name");
		TaskGroupBean    mapped   = toBean(group , context);
		TaskGroupDTO     reMapped = toDTO(mapped, context);

		assertIs(mapped, reMapped);
	}

	@Test void withSubTasks()
	{
		String name  = "name";
		int    count = 3;

		TaskGroupDTO group = createGroup(name);
		createTasks(group, 3);

		assertThat(group.tasks().isPresent() , is(true));
		assertThat(group.tasks().get().size(), is(count));
	}

	void assertIs(TaskGroupBean bean, TaskGroupDTO dto)
	{
		assertThat(bean.id               (), is(dto.id               ()));
		assertThat(bean.version          (), is(dto.version          ()));
		assertThat(bean.name             (), is(dto.name             ()));
		assertThat(bean.description      (), is(dto.description      ()));
		assertThat(bean.tasks().isPresent(), is(dto.tasks().isPresent()));

		bean.tasks().ifPresent(ts -> assertThat(ts.size(), is(dto.tasks().get().size())));
	}

	private TaskGroupDTO createGroup(                    String name) { return new TaskGroupDTO(name);   }
	private void         createTask (TaskGroupDTO group, String name) {        new TaskDTO(group, name); }
	private void         createTasks(TaskGroupDTO group, int count  )
	{
		for (int i = 0; i < count; i++) createTask(group, "task " + i);
	}
}