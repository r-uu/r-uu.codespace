package de.ruu.app.jeeeraaah.client.ws.rs.demo;

import de.ruu.app.jeeeraaah.client.ws.rs.TaskGroupServiceClient;
import de.ruu.app.jeeeraaah.client.ws.rs.TaskServiceClient;
import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.lib.cdi.se.CDIContainer;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
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
		log.debug("initialised ws.rs-clients successfully: {}", not(isNull(taskGroupService)) && not(isNull(taskService)));
	}

	public void run() throws Exception { populate(); }

	private void populate() throws Exception
	{
		TaskGroupBean group = new TaskGroupBean("group." + ThreadLocalRandom.current().nextInt());
		group = taskGroupService.create(group);

		TaskBean main = new TaskBean(group, "main." + ThreadLocalRandom.current().nextInt());
		TaskBean sub  = new TaskBean(group, "sub."  + ThreadLocalRandom.current().nextInt());

		main = taskService.create(main);
		sub  = taskService.create(sub );

		taskService.addSubTask(main, sub);
	}

	public static void main(String[] args) throws Exception
	{
		CDIContainer.bootstrap(DBPopulateTiny.class.getClassLoader());
		DBPopulateTiny populate = CDI.current().select(DBPopulateTiny.class).get();
		populate.run();
	}
}