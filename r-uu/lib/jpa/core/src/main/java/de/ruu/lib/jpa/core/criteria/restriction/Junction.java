package de.ruu.lib.jpa.core.criteria.restriction;

import de.ruu.lib.jpa.core.criteria.Criteria;
import de.ruu.lib.jpa.core.criteria.Criteria.CriteriaQuery;
import de.ruu.lib.jpa.core.criteria.Criterion;

import java.util.ArrayList;
import java.util.List;

/** Junction expression */
public class Junction implements Criterion
{
	private final List<Criterion> criteria = new ArrayList<Criterion>();
	private final String          operator;

	protected Junction(String operator)
	{
		this.operator = operator;
	}

	/**
	 * Add criterion to junction.
	 *
	 * @param criterion criterion
	 * @return junction
	 */
	public Junction add(Criterion criterion)
	{
		criteria.add(criterion);
		return this;
	}

	@Override public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
	{
		if (this.criteria.size() == 0) return "1=1";

		StringBuilder result = new StringBuilder("(");

		for (Criterion criterion : this.criteria)
		{
			if (result.length() > 1)
			{
				result.append(" " + operator + " ");
			}
			result.append(criterion.toSqlString(criteria, criteriaQuery));
		}

		return result.append(")").toString();
	}
}