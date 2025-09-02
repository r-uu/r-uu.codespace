package de.ruu.app.jeeeraaah.common.jpa.ee;

import de.ruu.app.jeeeraaah.common.jpa.TaskRepositoryJPA;
import de.ruu.app.jeeeraaah.common.jpa.TaskServiceJPA;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.nonNull;

@ApplicationScoped
@Transactional // jakarta.transaction.Transactional interceptor
@Slf4j
public class TaskServiceJPAEE extends TaskServiceJPA
{
	@Inject private TaskRepositoryJPAEE repository;

	@PostConstruct private void postConstruct() { log.debug("repository available: {}", nonNull(repository)); }

	@Override protected TaskRepositoryJPA repository() { return repository; }
}