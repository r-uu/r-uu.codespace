package de.ruu.lib.jpa.se.eclipselink;

import de.ruu.lib.jpa.core.AbstractPersistenceUnitInfo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.persistence.jpa.PersistenceProvider;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.List;

import static java.util.Objects.isNull;

/** persistence unit info with eclipselink as persistence provider */
@Slf4j
public class PersistenceUnitInfo extends AbstractPersistenceUnitInfo
{
	private URL persistenceUnitRootURL; // initialised lazily in persistenceUnitRootURL()

	public PersistenceUnitInfo(String persistenceUnitName, DataSource dataSource)
	{
		super(persistenceUnitName, PersistenceProvider.class, dataSource);
		persistenceUnitRootURL(persistenceUnitRootURL()); // enforce lazy initialisation
	}

	public void persistenceUnitRootURL(URL url) { persistenceUnitRootURL = url; }

	@Override public String getScopeAnnotationName() { return null; }

	@Override public List<String> getQualifierAnnotationNames() { return List.of(); }

	/** enforce lazy initialisation */
	@Override public URL getPersistenceUnitRootUrl() { return persistenceUnitRootURL(); }

	public URL persistenceUnitRootURL()
	{
		if (persistenceUnitRootURL != null) return persistenceUnitRootURL;

		CodeSource codeSource = getClass().getProtectionDomain().getCodeSource();

		if (isNull(codeSource))
		{
			// TODO: make this a config-property
			String spec = "file://";
			try
			{
				persistenceUnitRootURL = new URL(spec);
			}
			catch (MalformedURLException e)
			{
				throw new IllegalStateException("persistence unit root url " + spec + " is malformed");
			}
		}
		else
		{
			persistenceUnitRootURL = codeSource.getLocation();
			log.debug("persistence unit root url {}", persistenceUnitRootURL);
		}

		return persistenceUnitRootURL;
	}
}