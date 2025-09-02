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

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static de.ruu.lib.util.BooleanFunctions.not;
import static java.util.Objects.isNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@DisabledOnServerNotListening(propertyNameHost = "jeeeraaah.rest-api.host", propertyNamePort = "jeeeraaah.rest-api.port")
@Slf4j
class TestTaskServiceClient
{
	private static SeContainer seContainer; // initialisation and closure handled in before/after all methods

	private TaskServiceClient taskServiceClient;

	@BeforeAll static void beforeAll()
	{
		log.debug("cdi container initialisation");
		try
		{
			seContainer =
					SeContainerInitializer
							.newInstance()
							.addExtensions(CDIExtension.class)
							.initialize();
			CDIContainer.bootstrap(TestTaskServiceClient.class.getClassLoader());
		}
		catch (Exception e)
		{
			log.error("failure initialising seContainer", e);
		}
		log.debug("cdi container initialisation successful: {}", not(isNull(seContainer)));
	}

	@BeforeEach void beforeEach()
	{
		taskServiceClient = CDI.current().select(TaskServiceClient.class).get();
		log.debug("rs-client initialisation successful: {}", not(isNull(taskServiceClient)));
	}

	@Test void testCreate()
	{
		TaskGroupServiceClient taskGroupServiceClient = CDI.current().select(TaskGroupServiceClient.class).get();
		assertThat("clientTaskGroup could not be initialised", taskGroupServiceClient, is(notNullValue()));

		String             name     = "test group - " + ThreadLocalRandom.current().nextInt();
		TaskGroupEntityDTO groupIn  = new TaskGroupEntityDTO(name);
		TaskGroupEntityDTO groupOut = taskGroupServiceClient.create(groupIn);
		log.debug("group in : {}", groupIn);
		log.debug("group out: {}", groupOut);

		assertThat("wrong value in groupOut.name", groupOut.name(), is(name));
		assertThat("groupOut.id      is null", groupOut.     id(),  is(notNullValue()));
		assertThat("groupOut.version is null", groupOut.version(),  is(notNullValue()));

		for (int i = 0; i < 3; i++)
		{
			name = "test task - " + i + "." + ThreadLocalRandom.current().nextInt();
			TaskEntityDTO taskIn  = new TaskEntityDTO(groupOut, name);
			TaskEntityDTO taskOut = taskServiceClient.create(taskIn);
			log.debug("task in : {}", taskIn);
			log.debug("task out: {}", taskOut);
		}
	}

	@Test void testFindAll()
	{
		Set<TaskEntityDTO> all = taskServiceClient.findAll();
		StringBuilder sb = StringBuilders.sb("tasks");
		all.forEach(tg -> sb.append("\n").append(tg));
		log.debug(sb.toString());
	}
}