package de.ruu.lib.jpa.core;

import de.ruu.lib.jpa.core.criteria.Criteria;
import de.ruu.lib.jpa.core.criteria.restriction.Restrictions;
import de.ruu.lib.util.Reflection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;
import lombok.NonNull;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract implementation of generic DAO.
 * <p>
 * Note: It is intentional that there is no automatic transaction handling here.
 *
 * @param <I> entity's primary key, it has to be {@link Serializable}
 * @param <T> entity type, it implements at least {@code Entity2<I>}
 * @see Entity
 *
 * @author r-uu
 */
public abstract class AbstractRepository<T extends Entity<I>, I extends Serializable>
		implements Repository<T, I>, AutoCloseable
{
	private final Class<Entity<I>> clazz;

	/**
	 * Determines value for {@link #clazz} from type arguments of implementing class by reflection. Also works for proxied
	 * instances in e.g. CDI environments.
	 */
	@SuppressWarnings(value = "unchecked")
	protected AbstractRepository()
	{
		Optional<ParameterizedType> optional = Reflection.getFirstParameterizedTypeInParents(getClass());

		if (optional.isPresent())
		{
			ParameterizedType parameterizedTypeForAbstractDAO = optional.get();

			Type type = parameterizedTypeForAbstractDAO.getActualTypeArguments()[0];

			try
			{
				clazz = (Class<Entity<I>>) Class.forName(type.getTypeName());
			}
			catch (ClassNotFoundException e)
			{
				throw new IllegalStateException("could not find class " + type.getTypeName(), e);
			}
		}
		else
		{
			throw new IllegalStateException("could not lookup parameterized type in parents");
		}
	}

	protected abstract EntityManager entityManager();

	@Override public Class<Entity<I>> entityClass() { return clazz; }

	@SuppressWarnings(value = "unchecked")
	@Override
	public Optional<T> find(@NonNull I id) { return Optional.ofNullable((T) entityManager().find(clazz, id)); }

	@SuppressWarnings(value = "unchecked")
	@Override
	public List<T> find(I... ids)
	{
		return findByCriteria(Criteria.forClass(clazz).add(Restrictions.in(Entity.P_ID, ids)));
	}

	@SuppressWarnings(value = "unchecked")
	@Override
	public List<T> findAll() { return findByCriteria(Criteria.forClass(clazz)); }

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findByNamedQuery(final String name, Object... params) {

		Query query = entityManager().createNamedQuery(name);

		for (int i = 0; i < params.length; i++)
		{
			query.setParameter(i + 1, params[i]);
		}

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override public List<T> findByNamedQueryAndNamedParams(final String name, final Map<String, ?> params)
	{
		Query query = entityManager().createNamedQuery(name);

		params.forEach((key, value) -> query.setParameter(key, value));

		return query.getResultList();
	}

	@Override public Long countAll()
	{
		return
				entityManager()
						.createQuery("select count(*) from " + clazz.getSimpleName(), Long.class)
						.getSingleResult();
	}

	@Override public boolean isDetached(final T object)
	{
		if (object.optionalId().isPresent())             // must not be transient
		{
			if (entityManager().contains(object) == false) // must not be managed now
			{
				if (find(object.getId()) != null)            // must not be removed
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Saves the given object to the database. If the object has an ID, it will be merged; otherwise, it will be persisted.
	 *
	 * @param object
	 * @return the saved object, which may be a different instance than the one passed in
	 * @throws IllegalArgumentException if the object is null or does not have a valid ID
	 * @throws IllegalStateException if the entity manager is not open or the transaction is not active
	 * @see EntityManager#persist(Object)
	 * @see EntityManager#merge(Object)
	 */
	@Override public @NonNull T save(@NonNull T object)
	{
		EntityManager entityManager = entityManager();

		if (object.getId() != null) object = entityManager.merge(object);
		else                                 entityManager.persist(object);

		// no entityManager.flush() here, because it is not necessary
		// and it may cause problems with optimistic locking in some cases

		return object;
	}

//	@Override public T save(final T object)
//	{
//		T result;
//		EntityManager entityManager = entityManager();
//
//		if (object.getId() != null)
//		{
//			result = entityManager.merge(object);
//		}
//		else
//		{
//			entityManager.persist(object);
//			result = object;
//		}
//
//		entityManager.flush(); // execute flush to update version to the correct value
//
//		return result;
//	}

	//	@SafeVarargs
	@Override public void save(final T... entities)
	{
		for (T entity : entities)
		{
			save(entity);
		}
	}

	@Override public boolean delete(final I id)
	{
		Optional<T> optional = find(id);

		if (optional.isPresent())
		{
			delete(optional.get());
			return true;
		}

		return false;
	}

	@Override public boolean delete(T entity)
	{
		entityManager().remove(entity);
		return true;
	}

	@Override public void refresh(final T entity)
	{
		entityManager().refresh(entity);
	}

	@Override public void flushAndClear()
	{
		entityManager().flush();
		entityManager().clear();
	}

	/**
	 * Retrieve objects using criteria. It is equivalent to <code>criteria.list(entityManager)</code>.
	 *
	 * @param criteria criteria which will be executed
	 * @return list of founded objects
	 * @see Query#getResultList()
	 */
	protected List findByCriteria(Criteria criteria)
	{
		return criteria.list(entityManager());
	}

	/**
	 * Retrieve an unique object using criteria. It is equivalent to
	 * <code>criteria.uniqueResult(entityManager)</code>.
	 *
	 * @param criteria criteria which will be executed
	 * @return retrieved object
	 * @throws NoResultException - if there is no result
	 * @throws NonUniqueResultException - if more than one result
	 * @see Query#getSingleResult()
	 */
	protected Object findUniqueByCriteria(Criteria criteria) throws NonUniqueResultException, NoResultException
	{
		return criteria.uniqueResult(entityManager());
	}

	@Override public void close() throws Exception
	{
		EntityManager entityManager = entityManager();

		if (entityManager.isOpen())
		{
			entityManager.getTransaction().begin();
			entityManager.flush();
			entityManager.getTransaction().commit();
			entityManager.close();
		}
	}
}