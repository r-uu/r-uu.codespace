package de.ruu.lib.jdbc.postgres;

import org.junit.jupiter.api.Test;
import org.postgresql.Driver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

class TestJDBCProperties
{
	@Test void test()
	{
		String host     = "localhost";
		int    port     = 5432;
		String database = "lib_test";
		String user     = database;
		String password = database;

		JDBCProperties jdbcProperties =
				new JDBCProperties(
						new JDBCURL(host, port, database), user, password);

		assertThat(jdbcProperties, is(not(nullValue())));

		assertThat(jdbcProperties.user(),     is(user));
		assertThat(jdbcProperties.password(), is(password));
		assertThat(jdbcProperties.driver(),   is(Driver.class.getName()));
	}
}