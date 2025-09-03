package de.ruu.app.jeeeraaah.common.ee.jpa;

import de.ruu.app.jeeeraaah.common.jpa.TaskGroupRepositoryJPA;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupServiceJPA;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Transactional // jakarta.transaction.Transactional interceptor
@Slf4j
public class TaskGroupServiceJPAEE extends TaskGroupServiceJPA
{
	@Inject private TaskGroupRepositoryJPAEE repository;

//	@PostConstruct private void postConstruct() { log.debug("repository available: {}", nonNull(repository)); }

	@Override protected TaskGroupRepositoryJPA repository() { return repository; }
}