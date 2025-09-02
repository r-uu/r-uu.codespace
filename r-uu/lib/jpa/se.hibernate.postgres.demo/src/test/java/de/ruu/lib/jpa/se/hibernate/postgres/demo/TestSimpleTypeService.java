package de.ruu.lib.jpa.se.hibernate.postgres.demo;

import de.ruu.lib.cdi.common.CDIExtension;
import de.ruu.lib.jpa.se.hibernate.postgres.demo.EntityManagerProducer.TransactionalInterceptorCDI;
import de.ruu.lib.junit.DisabledOnServerNotListening;
import de.ruu.lib.util.BooleanFunctions;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Objects.isNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@DisabledOnServerNotListening(propertyNameHost = "database.host", propertyNamePort = "database.port")
@Slf4j
class TestSimpleTypeService
{
	private static SeContainer seContainer; // initialisation and closure handled in before/after all methods

	@SuppressWarnings("unchecked")
	@BeforeAll static void beforeAll()
	{
		log.debug("cdi container initialisation");
		try
		{
			seContainer =
					SeContainerInitializer
							.newInstance()
							.addExtensions     (CDIExtension.class               )
							.addBeanClasses    (TransactionalInterceptorCDI.class)
							.enableInterceptors(TransactionalInterceptorCDI.class)
							.initialize();
		}
		catch (Exception e)
		{
			log.error("failure initialising seContainer", e);
		}
		log.debug("cdi container initialisation successful: {}", BooleanFunctions.not(isNull(seContainer)));
	}

	@AfterAll static void afterAll()
	{
		log.debug("cdi container shut down");
		seContainer.close();
		log.debug("cdi container shut down {}", seContainer.isRunning() ? "unsuccessful" : "successful");
	}

	@Test void testSimpleTypeService()
	{
		SimpleTypeService service = seContainer.select(SimpleTypeService.class).get();
		assertThat(service, is(not(nullValue())));

		String name = "schmottekk";
		SimpleTypeEntity entity = new SimpleTypeEntity(name);
		entity = service.save(entity);
		assertThat(entity, is(not(nullValue())));
		assertThat(entity.id(), is(not(nullValue())));
		assertThat(entity.name(), is(name));

		name = "äffchen";
		entity.name(name);
		entity = service.save(entity);
		assertThat(entity, is(not(nullValue())));
		assertThat(entity.id(), is(not(nullValue())));
		assertThat(entity.name(), is(name));

		service.delete(entity.getId());
		Optional<SimpleTypeEntity> optional = service.find(entity.getId());
		assertThat(optional, is(not(nullValue())));
		assertThat(optional.isPresent(), is(false));
	}
}