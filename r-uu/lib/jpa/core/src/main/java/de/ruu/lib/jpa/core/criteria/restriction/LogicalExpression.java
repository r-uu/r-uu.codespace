package de.ruu.lib.jpa.core.criteria.restriction;

import de.ruu.lib.jpa.core.criteria.Criteria;
import de.ruu.lib.jpa.core.criteria.Criteria.CriteriaQuery;
import de.ruu.lib.jpa.core.criteria.Criterion;

/** Logical expression */
public class LogicalExpression implements Criterion
{

	private final Criterion lhs;
	private final Criterion rhs;
	private final String    operator;

	protected LogicalExpression(Criterion lhs, Criterion rhs, String operator)
	{
		this.lhs      = lhs;
		this.rhs      = rhs;
		this.operator = operator;
	}

	@Override public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
	{
		return
				  "(" + lhs.toSqlString(criteria, criteriaQuery) + " " + operator + " "
		          + rhs.toSqlString(criteria, criteriaQuery)
				+ ")";
	}
}