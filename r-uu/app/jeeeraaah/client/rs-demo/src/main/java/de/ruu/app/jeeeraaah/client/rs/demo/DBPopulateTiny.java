package de.ruu.app.jeeeraaah.client.rs.demo;

import de.ruu.app.jeeeraaah.client.rs.TaskGroupServiceClient;
import de.ruu.app.jeeeraaah.client.rs.TaskServiceClient;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.lib.cdi.se.CDIContainer;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

import static de.ruu.lib.util.BooleanFunctions.not;
import static java.util.Objects.isNull;

@Slf4j
public class DBPopulateTiny
{
	@Inject private TaskGroupServiceClient taskGroupService;
	@Inject private TaskServiceClient      taskService;

	@PostConstruct private void postConstruct()
	{
		log.debug("initialised rs-clients successfully: {}", not(isNull(taskGroupService)) && not(isNull(taskService)));
	}

	public void run() { populate(); }

	private void populate()
	{
		TaskGroupEntityDTO group = new TaskGroupEntityDTO("group." + ThreadLocalRandom.current().nextInt());
		group = taskGroupService.create(group);

		TaskEntityDTO main = new TaskEntityDTO(group, "main." + ThreadLocalRandom.current().nextInt());
		TaskEntityDTO sub  = new TaskEntityDTO(group, "sub."  + ThreadLocalRandom.current().nextInt());

		main = taskService.create(main);
		sub  = taskService.create(sub );

		taskService.addSubTask(main, sub);
	}

	public static void main(String[] args)
	{
		CDIContainer.bootstrap(DBPopulateTiny.class.getClassLoader());
		DBPopulateTiny populate = CDI.current().select(DBPopulateTiny.class).get();
		populate.run();
	}
}