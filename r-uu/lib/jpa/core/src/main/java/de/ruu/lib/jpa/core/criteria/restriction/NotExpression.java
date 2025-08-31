package de.ruu.lib.jpa.core.criteria.restriction;

import de.ruu.lib.jpa.core.criteria.Criteria;
import de.ruu.lib.jpa.core.criteria.Criteria.CriteriaQuery;
import de.ruu.lib.jpa.core.criteria.Criterion;

/** not expression */
public class NotExpression implements Criterion
{
	private Criterion criterion;

	protected NotExpression(Criterion criterion)
	{
		this.criterion = criterion;
	}

	@Override public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
	{
		return "not (" + criterion.toSqlString(criteria, criteriaQuery) + ')';
	}
}