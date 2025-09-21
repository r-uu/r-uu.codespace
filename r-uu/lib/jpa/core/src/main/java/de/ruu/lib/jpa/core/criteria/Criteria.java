package de.ruu.lib.jpa.core.criteria;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Criteria used to build EQL queries. */
public class Criteria<T>
{
	// Helper method to get the actual type parameter at runtime
	@SuppressWarnings("unchecked")
	protected Class<T> getType()
	{
		if (this instanceof Subcriteria)
		{
			return ((Subcriteria<T>) this).getParentCriteria().getType();
		}
		return (Class<T>) Object.class;
	}

	/**
	 * Join types which can be used to <code>addAlias</code> and <code>createCriteria</code> methods.
	 */
	public enum JoinType
	{
		/** Inner join. Default one. */
		INNER_JOIN("inner join"),
		/** Left outer join. */
		LEFT_JOIN("left outer join");

		private final String sql;
		JoinType(String sql) { this.sql = sql;}
		public String toSqlString() { return sql; }
	}

	private int aliasNumber;
	private final String name;
	private final String alias;
	private final Criteria<T> parent;
	private final JoinType joinType;
	private Projection<T> projection;
	private Criteria<T> projectionCriteria;
	private final List<CriterionEntry<T>> criterionList = new ArrayList<>();
  private final List<OrderEntry<T>> orderList = new ArrayList<>();
  private final List<Subcriteria<T>> subcriteriaList = new ArrayList<>();

	// Getter methods for lists to ensure type safety
	protected List<CriterionEntry<T>> getCriterionList()
	{
		return criterionList;
	}

	protected List<OrderEntry<T>> getOrderList()
	{
		return orderList;
	}

	protected List<Subcriteria<T>> getSubcriteriaList()
	{
		return subcriteriaList;
	}
	private Integer maxResults;
	private Integer firstResult;

	/** Create new criteria for specified <code>IEntity</code> implementation. */
	@SuppressWarnings("unchecked")
    public static <T> Criteria<T> forClass(Class<T> entity) {
        return new Criteria<T>(getEntityName(entity), "this", null, null) {
            @Override
            @SuppressWarnings("unchecked")
            protected Class<T> getType() {
                return entity;
            }
        };
    }

	private Criteria(String name, String alias, JoinType joinType, Criteria<T> parent)
	{
		this.name = name;
		this.alias = alias;
		this.parent = parent;
		this.joinType = joinType;
	}

	protected String getName() { return name; }
	protected Criteria<T> getParent() { return parent; }
	protected String getAlias() { return alias; }

	/**
	 * Get join type.
	 *
	 * @return join type
	 */
	protected JoinType getJoinType()
	{
		return joinType;
	}

	/**
	 * Specify that the query results will be a projection. The individual components contained within the given
	 * <code>Projection</code> determines the overall "shape" of the query result.
	 *
	 * @param projection projection used in query
	 * @return criteria object
	 * @see Projection
	 */
	public Criteria<T> setProjection(Projection<T> projection)
	{
		this.projection = projection;
		this.projectionCriteria = this;
		return this;
	}

	/**
	 * Add a <code>Criterion</code> to constrain the results to be retrieved.
	 *
	 * @param criterion new restriction
	 * @return criteria object
	 */
	public Criteria<T> add(Criterion<T> criterion)
	{
		criterionList.add(new CriterionEntry<T>(criterion, this));
		return this;
	}

	/**
	 * Add an <code>Order</code> to the result set.
	 *
	 * @param order new ordering
	 * @return criteria object
	 */
	public Criteria<T> addOrder(Order<T> order)
	{
		orderList.add(new OrderEntry<T>(order, this));
		return this;
	}

	/**
	 * Create a new <code>Criteria</code> joined using "inner join".
	 *
	 * @param name criteria entity name
	 * @return subcriteria
	 */
	public Criteria<T> createCriteria(String name)
	{
		return new Subcriteria<T>(name, createAlias(name), JoinType.INNER_JOIN, this);
	}

	/**
	 * Create a new <code>Criteria</code>.
	 *
	 * @param name     criteria entity name
	 * @param joinType join type
	 * @return subcriteria
	 */
	public Criteria<T> createCriteria(String name, JoinType joinType)
	{
		return new Subcriteria<T>(name, createAlias(name), joinType, this);
	}

	/**
	 * Create a new alias joined using "inner join".
	 *
	 * @param name  criteria entity name
	 * @param alias alias
	 * @return criteria
	 */
	public Criteria<T> createAlias(String name, String alias)
	{
		new Subcriteria<T>(name, alias, JoinType.INNER_JOIN, this);
		return this;
	}

	/**
	 * Create a new alias.
	 *
	 * @param name     criteria entity name
	 * @param alias    alias
	 * @param joinType join type
	 * @return criteria
	 */
	public Criteria<T> createAlias(String name, String alias, JoinType joinType)
	{
		new Subcriteria<T>(name, alias, joinType, this);
		return this;
	}

	/**
	 * Set a limit upon the number of objects to be retrieved.
	 *
	 * @param maxResults number of objects to be retrieved
	 * @return criteria object
	 */
	public Criteria<T> setMaxResults(int maxResults)
	{
		this.maxResults = Integer.valueOf(maxResults);
		return this;
	}

	/**
	 * Set the first result to be retrieved.
	 *
	 * @param firstResult first result to be retrieved
	 * @return criteria object
	 */
	public Criteria<T> setFirstResult(int firstResult)
	{
		this.firstResult = Integer.valueOf(firstResult);
		return this;
	}

	/**
	 * Get the results.
	 *
	 * @param entityManager entity manager
	 * @return list of retrieved objects
	 */
	public List<T> list(EntityManager entityManager)
	{
		return prepareQuery(entityManager).getResultList();
	}

	/**
	 * Convenience method to return a single instance that matches the query.
	 *
	 * @param entityManager entity manager
	 * @return retrieved object
	 * @throws NoResultException        - if there is no result
	 * @throws NonUniqueResultException - if more than one result
	 */
	@SuppressWarnings("unchecked")
    public T uniqueResult(EntityManager entityManager) throws NonUniqueResultException, NoResultException {
        List<T> results = list(entityManager);
        if (results.isEmpty()) {
            throw new NoResultException("No results found");
        }
        if (results.size() > 1) {
            throw new NonUniqueResultException("More than one result found: " + results.size());
        }
        return results.get(0);
    }

	@Override
	public String toString()
	{
		CriteriaQuery criteriaQuery = new CriteriaQuery();

		String result = prepateEql(criteriaQuery);

		if (criteriaQuery.getParams().size() > 0)
		{
			result += " [" + criteriaQuery.getParams() + "]";
		}

		return result;
	}

	protected final String createAlias(String name)
	{
		return name.replaceAll(".", "_") + "_" + aliasNumber++;
	}

	@SuppressWarnings("unchecked")
	private String prepateEql(CriteriaQuery criteriaQuery)
	{
		String sql = "from " + name + " as " + alias + " ";
		criteriaQuery.registerAlias(alias);

		for (Criteria<T> subcriteria : subcriteriaList)
		{
			sql += subcriteria.getJoinType().toSqlString() + " ";
			sql += criteriaQuery.getPropertyName(subcriteria.getName(), subcriteria.getParent());
			sql += " as " + subcriteria.getAlias() + " ";
			criteriaQuery.registerAlias(subcriteria.getAlias());
		}

		if (projection != null)
		{
			@SuppressWarnings("unchecked")
			String projectionSql = projection.toSqlString(projectionCriteria, criteriaQuery);
			if (projectionSql.length() > 0)
			{
				sql = "select " + projectionSql + " " + sql;
			}
			else
			{
				sql = "select this " + sql;
			}
		}
		else
		{
			sql = "select this " + sql;
		}

		String criterionSql = "";

		for (CriterionEntry<T> criterionEntry : criterionList)
		{
			if (criterionSql.length() > 0)
			{
				criterionSql += " and ";
			}
			criterionSql += criterionEntry.getCriterion().toSqlString(criterionEntry.getCriteria(), criteriaQuery);
		}

		if (criterionSql.length() > 0)
		{
			sql += "where " + criterionSql + " ";
		}

		if (projection != null)
		{
			if (projection.isGrouped())
			{
				@SuppressWarnings("unchecked")
				String groupBySql = projection.toGroupSqlString(projectionCriteria, criteriaQuery);
				if (groupBySql.length() > 0)
				{
					sql += "group by " + groupBySql + " ";
				}
			}
		}

		String orderSql = "";

		for (OrderEntry<T> orderEntry : orderList)
		{
			if (orderSql.length() > 0)
			{
				orderSql += ",";
			}
			orderSql += orderEntry.getOrder().toSqlString(orderEntry.getCriteria(), criteriaQuery);
		}

		if (orderSql.length() > 1)
		{
			sql += "order by " + orderSql + " ";
		}

		return sql.trim();
	}

	@SuppressWarnings("unchecked")
	private final Query prepareQuery(EntityManager entityManager)
	{
		CriteriaQuery criteriaQuery = new CriteriaQuery();

		String sql = prepateEql(criteriaQuery);

		Query query = entityManager.createQuery(sql);

		if (firstResult != null)
		{
			query.setFirstResult(firstResult);
		}

		if (maxResults != null)
		{
			query.setMaxResults(maxResults);
		}

		int i = 1;

		for (Object property : criteriaQuery.getParams())
		{
			query.setParameter(i++, property);
		}

		return query;
	}

	@SuppressWarnings("unchecked")
  private static <T> String getEntityName(Class<T> entity)
	{
    Entity entityAnnotation = entity.getAnnotation(Entity.class);
    return entityAnnotation == null ? entity.getSimpleName() : entityAnnotation.name();
	}

	/**
	 * Information about current query, for example parameters.
	 */
	public final class CriteriaQuery
	{

		private final List<Object> params = new ArrayList<Object>();

		private final Set<String> aliases = new HashSet<String>();

		CriteriaQuery()
		{
		}

		/**
		 * Get name of property in given criteria context.
		 *
		 * @param name     property's name
		 * @param criteria criteria
		 * @return proper name which can be used in EQL
		 */
		public String getPropertyName(String name, Criteria<T> criteria)
		{
			int pos = name.indexOf(".");

			if (pos == -1)
			{
				return criteria.getAlias() + "." + name;
			}
			else
			{
				if (aliases.contains(name.substring(0, pos)))
				{
					return name;
				}
				else
				{
					return criteria.getAlias() + "." + name;
				}
			}
		}

		/**
		 * Set query's param.
		 *
		 * @param param param value
		 */
		public void setParam(Object param)
		{
			params.add(param);
		}

		/**
		 * Register alias.
		 *
		 * @param alias alias
		 */
		void registerAlias(String alias)
		{
			this.aliases.add(alias);
		}

		/**
		 * Get all query's params.
		 *
		 * @return list of query's params
		 */
		List<Object> getParams()
		{
			return params;
		}
	}

	/**
	 * Subcritera associated with root criteria.
	 */
	public static final class Subcriteria<T> extends Criteria<T> {
    private final Criteria<T> parentCriteria;

    Subcriteria(String name, String alias, JoinType joinType, Criteria<T> parent) {
        super(name, alias, joinType, parent);
        this.parentCriteria = parent;
        parent.getSubcriteriaList().add(this);
    }
    
    protected Criteria<T> getParentCriteria() {
        return parentCriteria;
    }

    @Override
    public Criteria<T> add(Criterion<T> criterion) {
        getCriterionList().add(new CriterionEntry<>(criterion, this));
        return this;
    }

    @Override
    public Criteria<T> addOrder(Order<T> order) {
        getOrderList().add(new OrderEntry<>(order, this));
        return this;
    }

    @Override
    public Criteria<T> createCriteria(String name) {
        return new Subcriteria<>(name, createAlias(name), JoinType.INNER_JOIN, this);
    }

    @Override
    public Criteria<T> createCriteria(String name, JoinType joinType) {
        return new Subcriteria<>(name, createAlias(name), joinType, this);
    }

    @Override
    public Criteria<T> createAlias(String name, String alias) {
        new Subcriteria<>(name, alias, JoinType.INNER_JOIN, this);
        return this;
    }

    @Override
    public Criteria<T> createAlias(String name, String alias, JoinType joinType) {
        new Subcriteria<>(name, alias, joinType, this);
        return this;
    }

    @Override
    public List<T> list(EntityManager entityManager) {
        return parentCriteria.list(entityManager);
    }

    @Override
    public T uniqueResult(EntityManager entityManager) throws NonUniqueResultException, NoResultException {
        return parentCriteria.uniqueResult(entityManager);
    }

    @Override
    public Criteria<T> setFirstResult(int firstResult) {
        parentCriteria.setFirstResult(firstResult);
        return this;
    }

    @Override
    public Criteria<T> setMaxResults(int maxResults) {
        parentCriteria.setMaxResults(maxResults);
        return this;
    }

    @Override
    public Criteria<T> setProjection(Projection<T> projection) {
        parentCriteria.setProjection(projection);
        return this;
    }
	}

	private static final class CriterionEntry<T>
	{
		private final Criterion<T> criterion;
		private final Criteria<T> criteria;

		CriterionEntry(Criterion<T> criterion, Criteria<T> criteria)
		{
			this.criteria = criteria;
			this.criterion = criterion;
		}

		protected Criterion<T> getCriterion()
		{
			return criterion;
		}

		protected Criteria<T> getCriteria()
		{
			return criteria;
		}
	}

	private static final class OrderEntry<T>
	{
		private final Order<T> order;
		private final Criteria<T> criteria;

		OrderEntry(Order<T> order, Criteria<T> criteria)
		{
			this.criteria = criteria;
			this.order = order;
		}

		protected Order<T> getOrder()
		{
			return order;
		}

		protected Criteria<T> getCriteria()
		{
			return criteria;
		}
	}
}