package de.ruu.lib.jpa.core;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Generic DAO interface for common data access functionality
 *
 * @param <I> primary key's type
 * @param <T> object's type, it must implement at least {@link Entity}
 *
 * @author r-uu
 */
public interface Repository<T extends Entity<I>, I extends Serializable>
{
	/** @return {@code Class<T>} of entity */
	Class<? extends Entity<I>> entityClass();

	/**
	 * Retrieve a persisted object using the given id as primary key.
	 *
	 * @param id entity's primary key
	 * @return entity
	 */
	Optional<T> find(final I id);

	/**
	 * Retrieve a persisted objects using the given ids as primary keys.
	 *
	 * @param ids entity's ids
	 * @return list of entity
	 */
	@SuppressWarnings("unchecked")
	List<T> find(I... ids);

	/**
	 * Retrieve all persisted objects.
	 *
	 * @return list of entities
	 */
	List<T> findAll();

	/**
	 * Find using a named query.
	 * <p>
	 * Note that Named Queries are configured in the Entities and look like this:
	 *
	 * <pre>
	 *
	 * {@literal @}Entity2
	 * {@literal @}NamedQuery(name="findSalaryForNameAndDepartment",
	 *   query="SELECT e.salary " +
	 *         "FROM Employee e " +
	 *         "WHERE e.department.name = :deptName AND " +
	 *         "      e.name = :empName")
	 *  public class Employee {
	 *  ...
	 * </pre>
	 *
	 * @param queryName the name of the query
	 * @param params the query parameters
	 *
	 * @return the list of entities
	 */
	List<T> findByNamedQuery(final String queryName, Object... params);

	/**
	 * Count all entities.
	 *
	 * @return the number of entities
	 */
	Long countAll();

	/**
	 * Find using a named query.
	 *
	 * @param queryName the name of the query
	 * @param params the query parameters
	 *
	 * @return the list of entities
	 */
	List<T> findByNamedQueryAndNamedParams(final String queryName, final Map<String, ? extends Object> params);

	/**
	 * @param object
	 * @return {@code true} if {@code object} is not transient {@code and} is not managed and is not
	 *         removed
	 */
	boolean isDetached(final T object);

	/**
	 * Save all changes made to an entity.
	 *
	 * @param entity
	 */
	T save(T entity);

	/**
	 * Save all changes made to entities.
	 *
	 * @param entities
	 */
	@SuppressWarnings("unchecked")
	void save(T... entities);

	/**
	 * Remove an entity by given id.
	 *
	 * @param id entity's pk
	 */
	boolean delete(I id);

	/**
	 * Remove an entity.
	 *
	 * @param entity
	 */
	boolean delete(T entity);

	/**
	 * Refresh an entity that may have changed in another thread/transaction.
	 *
	 * @param entity transient entity
	 */
	void refresh(T entity);

	/**
	 * Write to database anything that is pending operation and clear it.
	 */
	void flushAndClear();
}