package de.ruu.lib.jpa.core.criteria.restriction;

import de.ruu.lib.jpa.core.criteria.Criteria;
import de.ruu.lib.jpa.core.criteria.Criteria.CriteriaQuery;
import de.ruu.lib.jpa.core.criteria.Criterion;

/** null expression */
public class NullExpression implements Criterion
{
	private final String property;

	protected NullExpression(String property)
	{
		this.property = property;
	}

	@Override public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
	{
		return criteriaQuery.getPropertyName(property, criteria) + " is null";
	}
}