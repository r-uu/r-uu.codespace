package de.ruu.lib.jpa.se.hibernate.postgres.demo;

import de.ruu.lib.jpa.se.hibernate.postgres.AbstractEntityManagerProducer;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import lombok.NoArgsConstructor;

import java.util.List;

@Singleton public class EntityManagerProducer extends AbstractEntityManagerProducer
{
	@Override public List<Class<?>> managedClasses() { return List.of(SimpleTypeEntity.class); }

	/**
	 * The {@link Produces} annotation makes CDI call this method when an {@link EntityManager} needs to be injected.
	 * @return entity manager created by {@link AbstractEntityManagerProducer}
	 */
	@HibernatePostgresDemoQualifier
	@Produces
	@Override public EntityManager produce() { return super.produce(); }
}