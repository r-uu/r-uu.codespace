package de.ruu.app.jeeeraaah.backend.persistence.jpa.ee;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupRepositoryJPA;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.nonNull;

@ApplicationScoped
@Slf4j
public class TaskGroupRepositoryJPAEE extends TaskGroupRepositoryJPA
{
	@PersistenceContext(unitName = "jeeeraaah_test") private EntityManager entityManager;

	@PostConstruct private void postConstruct() { log.debug("entity manager available: {}", nonNull(entityManager)); }

	@Override protected @NonNull EntityManager entityManager() { return entityManager; }
}