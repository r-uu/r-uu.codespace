package de.ruu.lib.jpa.core.criteria.restriction;

import de.ruu.lib.jpa.core.criteria.Criteria;
import de.ruu.lib.jpa.core.criteria.Criteria.CriteriaQuery;
import de.ruu.lib.jpa.core.criteria.Criterion;

public abstract class AbstractEmptynessExpression implements Criterion
{
	protected final String property;

	protected AbstractEmptynessExpression(String property)
	{
		this.property = property;
	}

	/** @return {@code true} if exclude is empty */
	protected abstract boolean excludeEmpty();

	@Override public final String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
	{
		return
				  (excludeEmpty()
				? "exists"
				: "not exists") + " (select 1 from " + criteriaQuery.getPropertyName(property, criteria) + ")";
	}
}