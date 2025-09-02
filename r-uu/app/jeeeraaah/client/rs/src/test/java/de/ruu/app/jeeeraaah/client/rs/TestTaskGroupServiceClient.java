package de.ruu.app.jeeeraaah.client.rs;

import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.lib.cdi.common.CDIExtension;
import de.ruu.lib.cdi.se.CDIContainer;
import de.ruu.lib.junit.DisabledOnServerNotListening;
import de.ruu.lib.util.StringBuilders;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.inject.spi.CDI;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisabledOnServerNotListening(propertyNameHost = "jeeeraaah.rest-api.host", propertyNamePort = "jeeeraaah.rest-api.port")
@Slf4j
class TestTaskGroupServiceClient
{
	private static SeContainer seContainer; // initialisation and closure handled in before/after all methods

	private TaskGroupServiceClient taskGroupServiceClient;

	@BeforeAll static void beforeAll() throws ClassNotFoundException
	{
		log.debug("cdi container initialisation");
		try
		{
			seContainer =
					SeContainerInitializer
							.newInstance()
							.addExtensions(CDIExtension.class)
							.initialize();
			CDIContainer.bootstrap(TestTaskGroupServiceClient.class.getClassLoader());
		}
		catch (Exception e)
		{
			log.error("failure initialising seContainer", e);
		}
		log.debug("cdi container initialisation {}", seContainer == null ? "unsuccessful" : "successful");
	}

	@BeforeEach void beforeEach()
	{
		taskGroupServiceClient = CDI.current().select(TaskGroupServiceClient.class).get();
		log.debug("initialised rs-client: clientTaskGroup == null -> {}", taskGroupServiceClient == null);
	}

	@Test void testCreate()
	{
		String name = "test - " + ThreadLocalRandom.current().nextInt();
		TaskGroupEntityDTO groupIn  = new TaskGroupEntityDTO(name);
		TaskGroupEntityDTO groupOut = taskGroupServiceClient.create(groupIn);
		log.debug("in : {}", groupIn);
		log.debug("out: {}", groupOut);

		assertThat("wrong value in groupOut.name", groupOut.name(), is(name));
		assertThat("groupOut.id      is null", groupOut.     id(),  is(notNullValue()));
		assertThat("groupOut.version is null", groupOut.version(),  is(notNullValue()));
	}

	@Test void testFindWithTasks()
	{
		TaskServiceClient taskServiceClient = CDI.current().select(TaskServiceClient.class).get();

		String name = "test - " + ThreadLocalRandom.current().nextInt();
		TaskGroupEntityDTO groupIn  = new TaskGroupEntityDTO(name);
		TaskGroupEntityDTO groupOut = taskGroupServiceClient.create(groupIn);

		for (int i = 0; i < 3; i++)
		{
			name = "test task - " + i + "." + ThreadLocalRandom.current().nextInt();
			TaskEntityDTO taskIn  = new TaskEntityDTO(groupOut, name);
			TaskEntityDTO taskOut = taskServiceClient.create(taskIn);
			log.debug("task in : {}", taskIn);
			log.debug("task out: {}", taskOut);
		}

		Optional<TaskGroupEntityDTO> optional = taskGroupServiceClient.findWithTasks(groupOut.id());
		assertThat("task group could not be found for id " + groupOut.id(), optional.isPresent(), is(true));
		groupOut = optional.get();
		assertThat("no tasks for group with id " + groupOut.id(), groupOut.tasks().isPresent());
		assertThat("wrong number of tasks"                      , groupOut.tasks().get().size(), is(3));
	}

	@Test void testFindAll()
	{
		Set<TaskGroupEntityDTO> all = taskGroupServiceClient.findAll();
		StringBuilder sb = StringBuilders.sb("groups");
		all.forEach(tg -> sb.append("\n").append(tg));
		log.debug(sb.toString());
	}
}