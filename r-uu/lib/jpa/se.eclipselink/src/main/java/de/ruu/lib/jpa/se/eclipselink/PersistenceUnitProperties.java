package de.ruu.lib.jpa.se.eclipselink;

import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_DRIVER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_PASSWORD;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_URL;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_USER;
import static org.eclipse.persistence.config.PersistenceUnitProperties.LOGGING_LEVEL;
import static org.eclipse.persistence.config.PersistenceUnitProperties.LOGGING_SESSION;
import static org.eclipse.persistence.config.PersistenceUnitProperties.LOGGING_THREAD;
import static org.eclipse.persistence.config.PersistenceUnitProperties.LOGGING_TIMESTAMP;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.logging.LogLevel;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(fluent = true)
public class PersistenceUnitProperties
{
	private Class<?> jdbcDriver;
	private String   jdbcURL;
	private String   jdbcUser;
	private String   jdbcPassword;
	private LogLevel loggingLevel;
	private boolean  loggingTimestamp;
	private boolean  loggingThread;
	private boolean  loggingSession;

	/** @return with @code {@code {@link #loggingLevel} == {@link LogLevel#INFO}} */
	public static PersistenceUnitProperties defaultProperties()
	{
		return
				PersistenceUnitProperties
						.builder()
								.loggingLevel(LogLevel.INFO)
						.build();
	}

	public Map<String, Object> properties()
	{
		Map<String, Object> result = new HashMap<>();

		result.put(JDBC_URL         , jdbcURL());
		result.put(JDBC_USER        , jdbcUser);
		result.put(JDBC_PASSWORD    , jdbcPassword);
		result.put(LOGGING_TIMESTAMP, "" + loggingTimestamp); // eclipselink fails with boolean properties
		result.put(LOGGING_THREAD   , "" + loggingThread);
		result.put(LOGGING_SESSION  , "" + loggingSession);

		if (jdbcDriver   != null) result.put(JDBC_DRIVER   , jdbcDriver.getName());
		if (loggingLevel != null) result.put(LOGGING_LEVEL , loggingLevel.getName());

		return Collections.unmodifiableMap(result);
	}
}