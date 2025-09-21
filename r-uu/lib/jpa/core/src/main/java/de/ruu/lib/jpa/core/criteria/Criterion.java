package de.ruu.lib.jpa.core.criteria;

import de.ruu.lib.jpa.core.criteria.Criteria.CriteriaQuery;

/** Criterion used for configurating where clauses */
public interface Criterion<T>
{
    /**
     * Generate part of SQL where clause with given criteria.
     *
     * @param criteria criteria used in criterion
     * @param criteriaQuery current query
     * @return part of select clause
     */
    public String toSqlString(Criteria<T> criteria, CriteriaQuery criteriaQuery) ;
}