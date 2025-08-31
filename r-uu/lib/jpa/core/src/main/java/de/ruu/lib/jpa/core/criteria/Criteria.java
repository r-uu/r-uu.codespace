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
public class Criteria
{
	/** Join types which can be used to <code>addAlias</code> and <code>createCriteria</code> methods. */
	public enum JoinType
	{
		/** Inner join. Default one. */
		INNER_JOIN("inner join"),

		/** Left outer join. */
		LEFT_JOIN("left outer join");

		private String sql;

		private JoinType(String sql)
		{
			this.sql = sql;
		}

		/** Generate EQL version of given join. */
		public String toSqlString()
		{
			return sql;
		}
	}

	private int aliasNumber;

	private String name;

	private String alias;

	private Criteria parent;

	private JoinType joinType;

	private Projection projection;

	private Criteria projectionCriteria;

	private List<CriterionEntry> criterionList = new ArrayList<CriterionEntry>();

	private List<OrderEntry> orderList = new ArrayList<OrderEntry>();

	private List<Subcriteria> subcriteriaList = new ArrayList<Subcriteria>();

	private Integer maxResults;

	private Integer firstResult;

	/** Create new criteria for specified <code>IEntity</code> implementation. */
	public final static Criteria forClass(Class<?> entity)
	{
		return new Criteria(getEntityName(entity), "this", null, null);
	}

	private Criteria(String name, String alias, JoinType joinType, Criteria parent)
	{
		this.name = name;
		this.alias = alias;
		this.parent = parent;
		this.joinType = joinType;
	}

	protected String getName()
	{
		return name;
	}

	/**
	 * Get parent criteria. If this is not an instance of <code>Subcriteria</code> method will return null.
	 *
	 * @return parent name
	 */
	protected Criteria getParent()
	{
		return parent;
	}

	/**
	 * Get criteria alias.
	 *
	 * @return alias
	 */
	protected String getAlias()
	{
		return alias;
	}

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
	public Criteria setProjection(Projection projection)
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
	public Criteria add(Criterion criterion)
	{
		criterionList.add(new CriterionEntry(criterion, this));
		return this;
	}

	/**
	 * Add an <code>Order</code> to the result set.
	 *
	 * @param order new ordering
	 * @return criteria object
	 */
	public Criteria addOrder(Order order)
	{
		orderList.add(new OrderEntry(order, this));
		return this;
	}

	/**
	 * Create a new <code>Criteria</code> joined using "inner join".
	 *
	 * @param name criteria entity name
	 * @return subcriteria
	 */
	public Criteria createCriteria(String name)
	{
		return new Subcriteria(name, createAlias(name), JoinType.INNER_JOIN, this);
	}

	/**
	 * Create a new <code>Criteria</code>.
	 *
	 * @param name criteria entity name
	 * @param joinType join type
	 * @return subcriteria
	 */
	public Criteria createCriteria(String name, JoinType joinType)
	{
		return new Subcriteria(name, createAlias(name), joinType, this);
	}

	/**
	 * Create a new alias joined using "inner join".
	 *
	 * @param name criteria entity name
	 * @param alias alias
	 * @return criteria
	 */
	public Criteria createAlias(String name, String alias)
	{
		new Subcriteria(name, alias, JoinType.INNER_JOIN, this);
		return this;
	}

	/**
	 * Create a new alias.
	 *
	 * @param name criteria entity name
	 * @param alias alias
	 * @param joinType join type
	 * @return criteria
	 */
	public Criteria createAlias(String name, String alias, JoinType joinType)
	{
		new Subcriteria(name, alias, joinType, this);
		return this;
	}

	/**
	 * Set a limit upon the number of objects to be retrieved.
	 *
	 * @param maxResults number of objects to be retrieved
	 * @return criteria object
	 */
	public Criteria setMaxResults(int maxResults)
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
	public Criteria setFirstResult(int firstResult)
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
	public List<?> list(EntityManager entityManager)
	{
		return prepareQuery(entityManager).getResultList();
	}

	/**
	 * Convenience method to return a single instance that matches the query.
	 *
	 * @param entityManager entity manager
	 * @return retrieved object
	 * @throws NoResultException - if there is no result
	 * @throws NonUniqueResultException - if more than one result
	 */
	public Object uniqueResult(EntityManager entityManager) throws NonUniqueResultException, NoResultException
	{
		return prepareQuery(entityManager).getSingleResult();
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

	private final String prepateEql(CriteriaQuery criteriaQuery)
	{
		String sql = "from " + name + " as " + alias + " ";
		criteriaQuery.registerAlias(alias);

		for (Criteria subcriteria : subcriteriaList)
		{
			sql += subcriteria.getJoinType().toSqlString() + " ";
			sql += criteriaQuery.getPropertyName(subcriteria.getName(), subcriteria.getParent());
			sql += " as " + subcriteria.getAlias() + " ";
			criteriaQuery.registerAlias(subcriteria.getAlias());
		}

		if (projection != null)
		{
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

		for (CriterionEntry criterion : criterionList)
		{
			if (criterionSql.length() > 0)
			{
				criterionSql += " and ";
			}
			criterionSql += criterion.getCriterion().toSqlString(criterion.getCriteria(), criteriaQuery);
		}

		if (criterionSql.length() > 0)
		{
			sql += "where " + criterionSql + " ";
		}

		if (projection != null)
		{
			if (projection.isGrouped())
			{
				String groupBySql = projection.toGroupSqlString(projectionCriteria, criteriaQuery);
				if (groupBySql.length() > 0)
				{
					sql += "group by " + groupBySql + " ";
				}
			}
		}

		String orderSql = "";

		for (OrderEntry order : orderList)
		{
			if (orderSql.length() > 0)
			{
				orderSql += ",";
			}
			orderSql += order.getOrder().toSqlString(order.getCriteria(), criteriaQuery);
		}

		if (orderSql.length() > 1)
		{
			sql += "order by " + orderSql + " ";
		}

		return sql.trim();
	}

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

	private final static String getEntityName(Class<?> type)
	{
		Entity entity = type.getAnnotation(Entity.class);

		if (entity == null || entity.name() == null || entity.name().length() == 0)
		{
			return type.getSimpleName();
		}
		else
		{
			return entity.name();
		}
	}

	/**
	 * Information about current query, for example parameters.
	 */
	public final class CriteriaQuery
	{

		private List<Object> params = new ArrayList<Object>();

		private Set<String> aliases = new HashSet<String>();

		private CriteriaQuery()
		{
		}

		/**
		 * Get name of property in given criteria context.
		 *
		 * @param name property's name
		 * @param criteria criteria
		 * @return proper name which can be used in EQL
		 */
		public String getPropertyName(String name, Criteria criteria)
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
		private void registerAlias(String alias)
		{
			this.aliases.add(alias);
		}

		/**
		 * Get all query's params.
		 *
		 * @return list of query's params
		 */
		private List<Object> getParams()
		{
			return params;
		}
	}

	/**
	 * Subcritera associated with root criteria.
	 */
	public final class Subcriteria
	    extends
	    Criteria
	{

		private Subcriteria(String name, String alias, JoinType joinType, Criteria parent)
		{
			super(name, alias, joinType, parent);
			Criteria.this.subcriteriaList.add(this);
		}

		@Override
		public Criteria add(Criterion criterion)
		{
			Criteria.this.criterionList.add(new CriterionEntry(criterion, this));
			return Subcriteria.this;
		}

		@Override
		public Criteria addOrder(Order order)
		{
			Criteria.this.orderList.add(new OrderEntry(order, this));
			return Subcriteria.this;
		}

		@Override
		public Criteria createCriteria(String name)
		{
			return new Subcriteria(name, Subcriteria.this.createAlias(name), JoinType.INNER_JOIN, Subcriteria.this);
		}

		@Override
		public Criteria createCriteria(String name, JoinType joinType)
		{
			return new Subcriteria(name, Subcriteria.this.createAlias(name), joinType, Subcriteria.this);
		}

		@Override
		public Criteria createAlias(String name, String alias)
		{
			new Subcriteria(name, alias, JoinType.INNER_JOIN, Subcriteria.this);
			return Subcriteria.this;
		}

		@Override
		public Criteria createAlias(String name, String alias, JoinType joinType)
		{
			new Subcriteria(name, alias, joinType, Subcriteria.this);
			return Subcriteria.this;
		}

		@Override
		public List<?> list(EntityManager entityManager)
		{
			return Criteria.this.list(entityManager);
		}

		@Override
		public Object uniqueResult(EntityManager entityManager) throws NonUniqueResultException, NoResultException
		{
			return Criteria.this.uniqueResult(entityManager);
		}

		@Override
		public Criteria setFirstResult(int firstResult)
		{
			Criteria.this.setFirstResult(firstResult);
			return Subcriteria.this;
		}

		@Override
		public Criteria setMaxResults(int maxResults)
		{
			Criteria.this.setMaxResults(maxResults);
			return Subcriteria.this;
		}

		@Override
		public Criteria setProjection(Projection projection)
		{
			Criteria.this.projection = projection;
			Criteria.this.projectionCriteria = this;
			return Subcriteria.this;
		}
	}

	/**
	 * Criteria entry.
	 */
	private final class CriterionEntry
	{

		private final Criterion criterion;

		private final Criteria criteria;

		private CriterionEntry(Criterion criterion, Criteria criteria)
		{
			this.criteria = criteria;
			this.criterion = criterion;
		}

		protected Criterion getCriterion()
		{
			return criterion;
		}

		protected Criteria getCriteria()
		{
			return criteria;
		}
	}

	/**
	 * Order entry
	 */
	private final class OrderEntry
	{

		private final Order order;

		private final Criteria criteria;

		private OrderEntry(Order order, Criteria criteria)
		{
			this.criteria = criteria;
			this.order = order;
		}

		protected Order getOrder()
		{
			return order;
		}

		protected Criteria getCriteria()
		{
			return criteria;
		}
	}
}