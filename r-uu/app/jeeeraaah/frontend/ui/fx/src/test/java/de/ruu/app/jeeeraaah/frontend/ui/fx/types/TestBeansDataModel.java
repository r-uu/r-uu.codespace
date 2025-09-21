package de.ruu.app.jeeeraaah.frontend.ui.fx.types;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class TestBeansDataModel
{
	private final static int GROUP_COUNT                   = 3;
	private final static int MAIN_TASKS_PER_GROUP_COUNT    = 3;
	private final static int SUB_TASKS_PER_MAIN_TASK_COUNT = 3;

	/*
	  - group 0
	    - main task 0.0
	      - sub task 0.0.1
	      - sub task 0.0.2
	      - sub task 0.0.3
	    - main task 0.1
	      - sub task 0.1.1
	      - sub task 0.1.2
	      - sub task 0.1.3
	    - main task 0.2
	      - sub task 0.2.1
	      - sub task 0.2.2
	      - sub task 0.2.3
	  - group 1
	    - main task 1.0
	      - sub task 1.0.1
	      - sub task 1.0.2
	      - sub task 1.0.3
	    - main task 1.1
	      - sub task 1.1.1
	      - sub task 1.1.2
	      - sub task 1.1.3
	    - main task 1.2
	      - sub task 1.2.1
	      - sub task 1.2.2
	      - sub task 1.2.3
	  - group 2
	    - main task 2.0
	      - sub task 2.0.1
	      - sub task 2.0.2
	      - sub task 2.0.3
	    - main task 2.1
	      - sub task 2.1.1
	      - sub task 2.1.2
	      - sub task 2.1.3
	    - main task 2.2
	      - sub task 2.2.1
	      - sub task 2.2.2
	      - sub task 2.2.3
	*/

	private Set<TaskGroupBean> taskGroups;

	@BeforeEach void beforeEach()
	{
		taskGroups = new DataFactory(GROUP_COUNT, MAIN_TASKS_PER_GROUP_COUNT, SUB_TASKS_PER_MAIN_TASK_COUNT).data();
	}

	@Test void testGroupBeanCount() { assertThat(taskGroups.size(), is(GROUP_COUNT)); }
	@Test void testTaskBeansPerGroupCount()
	{
		for (TaskGroupBean taskGroup : taskGroups)
		{
			Optional<Set<TaskBean>> optional = taskGroup.tasks();
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
		for (TaskGroupBean taskGroup : taskGroups)
		{
			Optional<Set<TaskBean>> optionalMainTasks = taskGroup.mainTasks();
			assertThat(optionalMainTasks.isPresent(), is(true));

			for (TaskBean mainTask : optionalMainTasks.get())
			{
				Optional<Set<TaskBean>> optionalSubTasks = mainTask.subTasks();
				assertThat(optionalSubTasks.isPresent (), is(true));
				assertThat(optionalSubTasks.get().size(), is(SUB_TASKS_PER_MAIN_TASK_COUNT));

				for (TaskBean subTask : optionalSubTasks.get())
				{
					// TODO ???
				}
			}

			assertThat(optionalMainTasks.get().size(), is(MAIN_TASKS_PER_GROUP_COUNT));
		}
	}
}