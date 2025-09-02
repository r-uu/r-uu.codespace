package de.ruu.lib.fx.comp;

import de.ruu.lib.cdi.common.CDIExtension;
import de.ruu.lib.util.classpath.ClasspathResource;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import javafx.application.Application;
import lombok.extern.slf4j.Slf4j;

import static jakarta.enterprise.inject.se.SeContainerInitializer.newInstance;

/**
 * Abstract base class for classes that launch JavaFX {@link Application}s with CDI support ({@link FXCApp}s).
 *
 * @author r-uu
 */
@Slf4j
public abstract class FXCAppRunner
{
	public static void run(Class<? extends FXCApp> appClass, String[] args, Runnable runBeforeAppLaunch)
	{
		if (appClass == null) throw new IllegalStateException("parameter appClass must not be null");

		log.debug("initialising CDI");

		checkIfBeansXMLIsPresent(appClass);

		final SeContainerInitializer initializer = newInstance();
		initializer.addExtensions(CDIExtension.class);

		try (SeContainer container = initializer.initialize())
		{
			if (runBeforeAppLaunch != null)
			{
				log.debug("calling runBeforeAppLaunch");
				runBeforeAppLaunch.run();
				log.debug("returned from runBeforeAppLaunch");
			}

			log.debug("starting application class: " + appClass.getName());
			Application.launch(appClass, args);
			log.debug("finished application class: " + appClass.getName());
		}

		log.debug("shut down CDI");
	}

	public static void run(Class<? extends FXCApp> appClass, String[] args)
	{
		run(appClass, args, null);
	}

	private final static String META_INF_BEANS_XML = "META-INF/beans.xml";

	/**
	 * logs a warning if META-INF/beans.xml can not be found in the same classpath location as <code>appClass</code>
	 *
	 * @param appClass
	 */
	private static void checkIfBeansXMLIsPresent(Class<?> appClass)
	{
		if (ClasspathResource.isResourceAvailableToClassLoaderOf(META_INF_BEANS_XML, appClass))
		{
			log.debug(appClass.getClassLoader().getResource(META_INF_BEANS_XML) + " found");
		}
		else
		{
			log.warn (appClass.getClassLoader().getResource(META_INF_BEANS_XML) + " not found");
		}
	}
}