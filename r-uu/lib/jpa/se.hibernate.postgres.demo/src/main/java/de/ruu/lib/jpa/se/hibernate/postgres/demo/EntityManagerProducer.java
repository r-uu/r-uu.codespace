package de.ruu.lib.jpa.se.hibernate.postgres.demo;

import de.ruu.lib.jpa.se.AbstractTransactionalInterceptor;
import de.ruu.lib.jpa.se.Transactional;
import de.ruu.lib.jpa.se.hibernate.postgres.AbstractEntityManagerProducer;
import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Qualifier;
import jakarta.inject.Singleton;
import jakarta.interceptor.Interceptor;
import jakarta.persistence.EntityManager;
import lombok.NoArgsConstructor;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static jakarta.interceptor.Interceptor.Priority.APPLICATION;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Singleton class EntityManagerProducer extends AbstractEntityManagerProducer
{
	@Override public List<Class<?>> managedClasses() { return List.of(SimpleTypeEntity.class); }

	/**
	 * The {@link Produces} annotation makes CDI call this method when an {@link EntityManager} needs to be injected.
	 * @return entity manager created by {@link AbstractEntityManagerProducer}
	 */
	@HibernatePostgresDemoQualifier
	@Produces
	@Override public EntityManager produce() { return super.produce(); }

	/** define an own interceptor for transactions */
	@Interceptor
	@Priority(APPLICATION) // this makes the interceptor available in other modules / archives
	@Transactional
	@NoArgsConstructor
	public static class TransactionalInterceptorCDI extends AbstractTransactionalInterceptor
	{
		@HibernatePostgresDemoQualifier
		@Inject private EntityManager entityManager;
		@Override protected EntityManager entityManager() { return entityManager; }
	}

	@Qualifier
	@Retention(RUNTIME)
	@Target({TYPE, FIELD, METHOD})
	@interface HibernatePostgresDemoQualifier { }
}