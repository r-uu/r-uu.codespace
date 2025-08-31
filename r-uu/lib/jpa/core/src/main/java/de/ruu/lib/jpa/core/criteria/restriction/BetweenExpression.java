package de.ruu.lib.jpa.core.criteria.restriction;

import de.ruu.lib.jpa.core.criteria.Criteria;
import de.ruu.lib.jpa.core.criteria.Criterion;

public class BetweenExpression implements Criterion
{
	private final String property;
	private final Object lo;
	private final Object hi;

	protected BetweenExpression(String property, Object lo, Object hi)
	{
		this.property = property;
		this.lo       = lo;
		this.hi       = hi;
	}

	@Override public String toSqlString(Criteria criteria, Criteria.CriteriaQuery criteriaQuery)
	{
		criteriaQuery.setParam(lo);
		criteriaQuery.setParam(hi);
		return criteriaQuery.getPropertyName(property, criteria) + " between ? and ?";
	}
}