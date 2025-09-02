package de.ruu.lib.jdbc.postgres;

import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

class TestJDBCURL
{
	@Test void test()
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
						.orElse("lib_test");;

		JDBCURL jdbcURL = new JDBCURL(databaseHost, databasePort, databaseName);

		assertThat(jdbcURL, is(not(nullValue())));

		assertThat(jdbcURL.host(),         is(databaseHost));
		assertThat(jdbcURL.port(),         is(databasePort));
		assertThat(jdbcURL.databaseName(), is(databaseName));

		assertThat(jdbcURL.asString(), is(JDBCURL.PROTOCOL + "://" + databaseHost + ":" + databasePort + "/" + databaseName));
	}
}