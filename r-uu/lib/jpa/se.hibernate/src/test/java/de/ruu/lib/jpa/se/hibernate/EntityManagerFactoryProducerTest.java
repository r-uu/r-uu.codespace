package de.ruu.lib.jpa.se.hibernate;

import de.ruu.lib.jdbc.postgres.DataSourceFactory;
import de.ruu.lib.jdbc.postgres.JDBCURL;
import de.ruu.lib.junit.DisabledOnServerNotListening;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

import static de.ruu.lib.jpa.se.hibernate.PersistenceUnitProperties.HBM2DLLAuto.CREATE;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@Slf4j
class EntityManagerFactoryProducerTest
{
	@DisabledOnServerNotListening(propertyNameHost = "database.host", propertyNamePort = "database.port")
	@Test void entityManagerFactoryProducer() throws SQLException
	{
		String databaseHost =
				ConfigProvider
						.getConfig()
						.getOptionalValue("database.host", String .class)
						.orElse("localhost");
		int    databasePort =
				ConfigProvider
						.getConfig()
						.getOptionalValue("database.port", Integer.class)
						.orElse(5432);
		String databaseName =
				ConfigProvider
						.getConfig()
						.getOptionalValue("database.name", String.class)
						.orElse("lib_test");
		String databaseUser =
				ConfigProvider
						.getConfig()
						.getOptionalValue("database.user", String.class)
						.orElse("lib_test");
		String databasePass =
				ConfigProvider
						.getConfig()
						.getOptionalValue("database.pass", String.class)
						.orElse("lib_test");

		String persistenceUnitName = "lib_test";

		JDBCURL jdbcURL = new JDBCURL(databaseHost, databasePort, databaseName);

		log.debug("jdbc url  : {}", jdbcURL);

		DataSource dataSource = new DataSourceFactory(jdbcURL, databaseUser, databasePass).create();

		log.debug("datasource: {}", dataSource);

		// save persistence unit info with hibernate as persistence provider
		PersistenceUnitInfo persistenceUnitInfo = new PersistenceUnitInfo(persistenceUnitName, dataSource);

		log.debug("persistence unit info\n{}", persistenceUnitInfo);

		// customise hibernate specifics
		PersistenceUnitProperties hibernateProperties =
				PersistenceUnitProperties
						.builder()
//								.dialect(PostgreSQLDialect.class)
								.formatSQL(true)
								.hbm2ddlAuto(CREATE)
								.jdbcDriver(org.postgresql.Driver.class)
								.jdbcURL(jdbcURL.asString())
								.showSQL(true)
						.build();

		log.debug("persistence unit properties\n{}", hibernateProperties);

		EntityManagerFactoryProducer producer =
				new EntityManagerFactoryProducer(persistenceUnitInfo, hibernateProperties);

		log.debug("entity manager factory producer\n{}", producer);

		EntityManagerFactory entityManagerFactory = producer.produce(databaseUser, databasePass);

		assertThat(entityManagerFactory, is(not(nullValue())));

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		assertThat(entityManager,                  is(not(nullValue())));
		assertThat(entityManager.getTransaction(), is(not(nullValue())));
	}
}