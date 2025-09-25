package de.ruu.app.jeeeraaah.backend.api.ws.rs.health;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.health.Startup;

import java.io.File;

@Singleton
@Startup
@Slf4j
public class ApplicationLifecycleListener
{
	private final static String DASHES = "-".repeat(50);

	@PostConstruct public void init()
	{
		log.info("{}\nworking directory {}\n{}", DASHES, new File(".").getAbsolutePath(), DASHES);
	}

	@PreDestroy public void destroy()
	{
		log.info("{}\nworking directory {}\n{}", DASHES, new File(".").getAbsolutePath(), DASHES);
	}
}