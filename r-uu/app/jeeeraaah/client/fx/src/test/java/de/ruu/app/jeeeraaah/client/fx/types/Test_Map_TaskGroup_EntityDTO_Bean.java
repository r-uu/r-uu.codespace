package de.ruu.app.jeeeraaah.client.fx.types;

import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.app.jeeeraaah.common.map.Map_TaskGroup_EntityDTO_Bean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class Test_Map_TaskGroup_EntityDTO_Bean
{
	@Test void standalone()
	{
		String             name = "name";
		TaskGroupEntityDTO dto  = createEntityDTO(name);

		assertThat(dto.name()             , is(name));
		assertThat(dto.tasks().isPresent(), is(false));
	}

	@Test void standaloneMapped()
	{
		String             name     = "name";
		TaskGroupEntityDTO dto      = createEntityDTO(name);
		TaskGroupBean        mapped = Map_TaskGroup_EntityDTO_Bean.INSTANCE.map(dto, new ReferenceCycleTracking());
		TaskGroupEntityDTO remapped = mapped.toDTO(new ReferenceCycleTracking());

		assertIs(dto     , mapped);
		assertIs(remapped, mapped);
	}

	@Test void withTasks()
	{
		String name  = "name";
		int    count = 3;

		TaskGroupEntityDTO group = createEntityDTO(name);
		createTasks(group, 3);

		assertThat(group.tasks().isPresent() , is(true));
		assertThat(group.tasks().get().size(), is(count));
	}

	static void assertIs(TaskGroupEntityDTO entityDTO, TaskGroupBean bean)
	{
		assertThat(entityDTO.id         (), is(bean.id         ()));
		assertThat(entityDTO.version    (), is(bean.version    ()));
		assertThat(entityDTO.name       (), is(bean.name       ()));
		assertThat(entityDTO.description(), is(bean.description()));

		assertThat(entityDTO.tasks().isPresent(), is(bean.tasks().isPresent()));

		entityDTO.tasks().ifPresent(ts -> assertThat(ts.size(), is(bean.tasks().get().size())));
	}

	private TaskGroupEntityDTO createEntityDTO(String name) { return new TaskGroupEntityDTO("name"); }

	private void createTask (TaskGroupEntityDTO group, String name) { new TaskEntityDTO(group, name); }
	private void createTasks(TaskGroupEntityDTO group, int count)
	{
		for (int i = 0; i < count; i++) createTask(group, "task " + i);
	}
}