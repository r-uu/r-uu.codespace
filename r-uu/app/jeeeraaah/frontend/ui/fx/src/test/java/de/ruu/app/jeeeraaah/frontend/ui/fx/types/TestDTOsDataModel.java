package de.ruu.app.jeeeraaah.frontend.ui.fx.types;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static de.ruu.app.jeeeraaah.common.api.mapping.Mappings.toDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TestDTOsDataModel
{
	private final static int GROUP_COUNT                   = 1;
	private final static int MAIN_TASKS_PER_GROUP_COUNT    = 1;
	private final static int SUB_TASKS_PER_MAIN_TASK_COUNT = 1;

	private Set<TaskGroupBean> groups;

	@BeforeEach void beforeEach()
	{
		groups = new DataFactory(GROUP_COUNT, MAIN_TASKS_PER_GROUP_COUNT, SUB_TASKS_PER_MAIN_TASK_COUNT).data();
	}

	@Test void testGroupBeanCount() { assertThat(groups.size(), is(GROUP_COUNT)); }
	@Test void testTaskBeansPerGroupCount()
	{
		for (TaskGroupBean group : groups)
		{
			Optional<Set<TaskBean>> optional = group.tasks();
			assertThat(optional.isPresent (), is(true));
			assertThat
			(
					optional.get().size(),
					is
					(
							// number of tasks has to be calculated
								MAIN_TASKS_PER_GROUP_COUNT                                 //                 each main task
							+ MAIN_TASKS_PER_GROUP_COUNT * SUB_TASKS_PER_MAIN_TASK_COUNT // each subtask of each main task
					)
			);
		}
	}
	@Test void testSubTaskBeansPerMainTaskBeans()
	{
		ReferenceCycleTracking context = new ReferenceCycleTracking();

		for (TaskGroupBean group : groups)
		{
			Optional<Set<TaskBean>> optionalMainTasks = group.mainTasks();
			assertThat(optionalMainTasks.isPresent(), is(true));

			for (TaskBean mainTask : optionalMainTasks.get())
			{
				Optional<Set<TaskBean>> optionalSubTasks = mainTask.subTasks();
				assertThat(optionalSubTasks.isPresent (), is(true));
				assertThat(optionalSubTasks.get().size(), is(SUB_TASKS_PER_MAIN_TASK_COUNT));

				for (TaskBean subTask : optionalSubTasks.get())
				{
					TaskDTO taskDTO = toDTO(subTask, context);
					assertThat("missing super task in mapped dto", taskDTO.superTask().isPresent(), is(true));
				}
			}

			assertThat(optionalMainTasks.get().size(), is(MAIN_TASKS_PER_GROUP_COUNT));
		}
	}
}