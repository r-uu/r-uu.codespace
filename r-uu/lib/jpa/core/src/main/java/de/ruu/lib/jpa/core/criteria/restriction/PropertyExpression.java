package de.ruu.lib.jpa.core.criteria.restriction;

import de.ruu.lib.jpa.core.criteria.Criteria;
import de.ruu.lib.jpa.core.criteria.Criterion;

/** property expression */
public class PropertyExpression implements Criterion
{
	private final String property;
	private final String otherProperty;
	private final String operator;

	protected PropertyExpression(String property, String otherProperty, String operator)
	{
		this.property      = property;
		this.otherProperty = otherProperty;
		this.operator      = operator;
	}

	@Override public String toSqlString(Criteria criteria, Criteria.CriteriaQuery criteriaQuery)
	{
		return
				  criteriaQuery.getPropertyName(property     , criteria) + operator
		    + criteriaQuery.getPropertyName(otherProperty, criteria);
	}
}