package de.ruu.lib.jpa.se.eclipselink;

import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_PASSWORD;
import static org.eclipse.persistence.config.PersistenceUnitProperties.JDBC_USER;

import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.eclipse.persistence.jpa.PersistenceProvider;

@RequiredArgsConstructor
public class EntityManagerFactoryProducer
{
	private final PersistenceUnitInfo       persistenceUnitInfo;
	private final PersistenceUnitProperties persistenceUnitProperties;

	public EntityManagerFactory produce(String username, String password)
	{
		Map <String, Object> properties = new HashMap<>(persistenceUnitProperties.properties());

		properties.put(JDBC_USER,		  username);
		properties.put(JDBC_PASSWORD, password);

		return
				new PersistenceProvider()
						.createContainerEntityManagerFactory(persistenceUnitInfo, properties);
//						.createEntityManagerFactory(persistenceUnitInfo.getPersistenceUnitName(), properties);
	}
}