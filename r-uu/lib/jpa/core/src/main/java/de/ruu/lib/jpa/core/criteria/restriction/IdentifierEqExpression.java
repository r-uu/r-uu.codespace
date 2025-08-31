package de.ruu.lib.jpa.core.criteria.restriction;

import de.ruu.lib.jpa.core.Entity;
import de.ruu.lib.jpa.core.criteria.Criteria;
import de.ruu.lib.jpa.core.criteria.Criteria.CriteriaQuery;
import de.ruu.lib.jpa.core.criteria.Criterion;

/** Identifier equal expression */
public class IdentifierEqExpression implements Criterion
{
	private final Object value;

	protected IdentifierEqExpression(Object value)
	{
		this.value = value;
	}

	@Override public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
	{
		criteriaQuery.setParam(value);
		return criteriaQuery.getPropertyName(Entity.P_ID, criteria) + " = ?";
	}
}