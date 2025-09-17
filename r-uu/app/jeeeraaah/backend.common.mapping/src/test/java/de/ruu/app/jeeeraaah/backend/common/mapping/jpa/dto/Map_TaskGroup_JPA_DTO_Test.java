package de.ruu.app.jeeeraaah.backend.common.mapping.jpa.dto;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

public class Map_TaskGroup_JPA_DTO_Test
{
	@Test void map_usesObjectFactory_andCopiesBasicFields()
	{
		// arrange
		String name = "group A";
		TaskGroupJPA group = new TaskGroupJPA(name);
		ReferenceCycleTracking context = new ReferenceCycleTracking();

		// act
		TaskGroupDTO dto = Map_TaskGroup_JPA_DTO.INSTANCE.map(group, context);

		// assert
		assertThat(dto, notNullValue());
		assertThat(dto.id(), nullValue());
		assertThat(dto.version(), nullValue());
		assertThat(dto.name(), is(name));
		assertThat("tasks should not be initialized by default", dto.tasks().isPresent(), is(false));
	}

	@Test void afterMapping_mapsRelatedTasks_intoContext_whenNotAlreadyMapped()
	{
		// arrange
		TaskGroupJPA group = new TaskGroupJPA("group B");
		TaskJPA      task1 = new TaskJPA(group, "task 1");
		TaskJPA      task2 = new TaskJPA(group, "task 2");
		ReferenceCycleTracking context = new ReferenceCycleTracking();

		// precondition
		assertThat(group.tasks().isPresent(), is(true));

		// act
		TaskGroupDTO dto = Map_TaskGroup_JPA_DTO.INSTANCE.map(group, context);

		// assert
		assertThat(dto, notNullValue());
		// afterMapping should ensure each related task is available in the context
		TaskDTO mappedT1 = context.get(task1, TaskDTO.class);
		TaskDTO mappedT2 = context.get(task2, TaskDTO.class);
		assertThat(mappedT1, notNullValue());
		assertThat(mappedT2, notNullValue());
	}

	@Test void afterMapping_skipsRemapping_whenTaskAlreadyInContext()
	{
		// arrange
		TaskGroupJPA group = new TaskGroupJPA("group C");
		TaskJPA task = new TaskJPA(group, "task 3");
		ReferenceCycleTracking context = new ReferenceCycleTracking();

		// pre-map task into context
		TaskDTO preMapped = Map_Task_JPA_DTO.INSTANCE.map(task, context);
		assertThat(preMapped, notNullValue());
		assertThat(context.get(task, TaskDTO.class), sameInstance(preMapped));

		// act: mapping the group should not override the existing mapping for task
		Map_TaskGroup_JPA_DTO.INSTANCE.map(group, context);

		// assert: mapping still the same instance
		TaskDTO post = context.get(task, TaskDTO.class);
		assertThat(post, sameInstance(preMapped));
	}
}
