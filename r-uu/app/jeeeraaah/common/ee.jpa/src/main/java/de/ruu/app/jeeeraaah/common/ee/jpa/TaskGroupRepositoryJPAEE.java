package de.ruu.app.jeeeraaah.common.ee.jpa;

import de.ruu.app.jeeeraaah.common.jpa.TaskGroupRepositoryJPA;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class TaskGroupRepositoryJPAEE extends TaskGroupRepositoryJPA
{
	@PersistenceContext(name = "jeeeraaah_test") private EntityManager entityManager;

//	@PostConstruct private void postConstruct() { log.debug("entity manager available: {}", nonNull(entityManager)); }

	@Override protected EntityManager entityManager() { return entityManager; }
}