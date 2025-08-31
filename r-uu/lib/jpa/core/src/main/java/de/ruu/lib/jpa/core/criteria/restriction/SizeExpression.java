package de.ruu.lib.jpa.core.criteria.restriction;

import de.ruu.lib.jpa.core.criteria.Criteria;
import de.ruu.lib.jpa.core.criteria.Criterion;

/** size expression */
public class SizeExpression implements Criterion
{
	private final String property;
	private final long   size;
	private final String operator;

	protected SizeExpression(String property, long size, String operator)
	{
		this.property = property;
		this.size     = size;
		this.operator = operator;
	}

	@Override public String toSqlString(Criteria criteria, Criteria.CriteriaQuery criteriaQuery)
	{
		criteriaQuery.setParam(size);
		return "? " + operator + " (select count(*) from " + criteriaQuery.getPropertyName(property, criteria) + ")";
	}
}