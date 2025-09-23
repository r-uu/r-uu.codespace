package de.ruu.lib.jpa.se.hibernate.postgres.demo;

import de.ruu.lib.cdi.common.CDIExtension;
import de.ruu.lib.cdi.se.CDIContainer;
import de.ruu.lib.junit.DisabledOnServerNotListening;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TransactionRequiredException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisabledOnServerNotListening(propertyNameHost = "database.host", propertyNamePort = "database.port")
@Slf4j class TestAbstractRepository
{
	/** bootstrap CDI in Java SE with {@code TransactionalInterceptorCDI} */
	@BeforeAll static void beforeAll()
	{
		CDIContainer.bootstrap(
				TestAbstractRepository.class.getClassLoader(),
				List.of(CDIExtension.class, EntityManagerProducer.class)); // explicitly register EntityManagerProducer
		log.debug("cdi bootstrapped successfully");
	}

	private SimpleTypeRepository repository;

	@BeforeEach void beforeEach()
	{
		repository = CDI.current().select(SimpleTypeRepository.class).get();
		assertThat(repository, is(not(nullValue())));
	}

	@Test void testAbstractRepositoryWithoutTransaction()
	{
		Exception exception =
				assertThrows(TransactionRequiredException.class, () ->
				{
					String name = "schmottekk";
					SimpleTypeEntity entity = new SimpleTypeEntity(name);
					entity = repository.create(entity);
					repository.entityManager().flush();
				});
	}

	@Test void testAbstractRepositoryWithTransaction()
	{
		EntityTransaction transaction = repository.entityManager().getTransaction();

		transaction.begin();

		String name = "schmottekk";
		SimpleTypeEntity entity = new SimpleTypeEntity(name);
		entity = repository.create(entity);
		assertThat(entity       , is(not(nullValue())));
		assertThat(entity.id()  , is(not(nullValue())));
		assertThat(entity.name(), is(name));

		name = "äffchen";
		entity.name(name);
		entity = repository.create(entity);
		assertThat(entity       , is(not(nullValue())));
		assertThat(entity.id()  , is(not(nullValue())));
		assertThat(entity.name(), is(name));

		repository.delete(entity.getId());
		Optional<SimpleTypeEntity> optional = repository.find(entity.getId());
		assertThat(optional            , is(not(nullValue())));
		assertThat(optional.isPresent(), is(false));

		transaction.commit();
	}
}