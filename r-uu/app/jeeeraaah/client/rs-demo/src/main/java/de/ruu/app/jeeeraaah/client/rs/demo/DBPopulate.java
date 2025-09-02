package de.ruu.app.jeeeraaah.client.rs.demo;

import de.ruu.app.jeeeraaah.client.rs.TaskServiceClient;
import de.ruu.app.jeeeraaah.client.rs.TaskGroupServiceClient;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.lib.cdi.se.CDIContainer;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import static de.ruu.lib.util.BooleanFunctions.not;
import static java.util.Objects.isNull;

@Slf4j
public class DBPopulate
{
	@Inject private TaskGroupServiceClient taskGroupServiceClient;
	@Inject private TaskServiceClient      taskServiceClient;

	@PostConstruct private void postConstruct()
	{
		log.debug ("initialised rs-clients successfully: {}", not(isNull(taskGroupServiceClient)) && not(isNull(taskServiceClient)));
	}

	public void run()
	{
		TaskGroupEntityDTO taskGroupProject = new TaskGroupEntityDTO("project jeeeraaah");
		taskGroupProject = taskGroupServiceClient.create(taskGroupProject);
		TaskEntityDTO taskFeatureSet1 = new TaskEntityDTO(taskGroupProject, "feature set 1");
		TaskEntityDTO taskFeatureSet2 = new TaskEntityDTO(taskGroupProject, "feature set 2");
		TaskEntityDTO taskFeatureSet3 = new TaskEntityDTO(taskGroupProject, "feature set 3");
		taskFeatureSet1.start(LocalDate.of(2025, 1,  1));
		taskFeatureSet1.end  (LocalDate.of(2025, 1, 31));
		taskFeatureSet2.start(LocalDate.of(2025, 2,  1));
		taskFeatureSet2.end  (LocalDate.of(2025, 2, 28));
		taskFeatureSet3.start(LocalDate.of(2025, 3,  1));
		taskFeatureSet3.end  (LocalDate.of(2025, 3, 31));
		taskFeatureSet1 = taskServiceClient.create(taskFeatureSet1);
		taskFeatureSet2 = taskServiceClient.create(taskFeatureSet2);
		taskFeatureSet3 = taskServiceClient.create(taskFeatureSet3);
		TaskEntityDTO taskFeature1    = new TaskEntityDTO(taskGroupProject, "feature 1");
		TaskEntityDTO taskFeature2    = new TaskEntityDTO(taskGroupProject, "feature 2");
		TaskEntityDTO taskFeature3    = new TaskEntityDTO(taskGroupProject, "feature 3");
		taskFeature1.start(LocalDate.of(2025, 1,  1));
		taskFeature1.end  (LocalDate.of(2025, 1, 31));
		taskFeature2.start(LocalDate.of(2025, 2,  1));
		taskFeature2.end  (LocalDate.of(2025, 2, 28));
		taskFeature3.start(LocalDate.of(2025, 3,  1));
		taskFeature3.end  (LocalDate.of(2025, 3, 31));
		taskFeature1    = taskServiceClient.create(taskFeature1);
		taskFeature2    = taskServiceClient.create(taskFeature2);
		taskFeature3    = taskServiceClient.create(taskFeature3);
//		taskFeatureSet1.addSubTask(taskFeature1);
//		taskFeatureSet1.addSubTask(taskFeature2);
//		taskFeatureSet1.addSubTask(taskFeature3);
		taskServiceClient.addSubTask(taskFeatureSet1, taskFeature1);
		taskServiceClient.addSubTask(taskFeatureSet2, taskFeature2);
		taskServiceClient.addSubTask(taskFeatureSet3, taskFeature3);
		taskServiceClient.addPredecessor(taskFeatureSet2, taskFeatureSet1);
		taskServiceClient.addSuccessor  (taskFeatureSet2, taskFeatureSet3);
//		taskServiceClient.addPredecessor(taskFeature2   , taskFeatureSet1);
	}

	public static void main(String[] args)
	{
		CDIContainer.bootstrap(DBPopulate.class.getClassLoader());
		DBPopulate populate = CDI.current().select(DBPopulate.class).get();
		populate.run();
	}
}