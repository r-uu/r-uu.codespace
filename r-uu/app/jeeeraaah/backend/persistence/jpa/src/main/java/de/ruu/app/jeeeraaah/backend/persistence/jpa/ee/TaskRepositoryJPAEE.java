package de.ruu.app.jeeeraaah.backend.persistence.jpa.ee;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskRepositoryJPA;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

@ApplicationScoped
@Slf4j
public class TaskRepositoryJPAEE extends TaskRepositoryJPA
{
	@PersistenceContext(name = "jeeeraaah") private EntityManager entityManager;

	@PostConstruct private void postConstruct() { log.debug("entity manager available: {}", nonNull(entityManager)); }

	@Override protected @NonNull EntityManager entityManager() { return entityManager; }
}