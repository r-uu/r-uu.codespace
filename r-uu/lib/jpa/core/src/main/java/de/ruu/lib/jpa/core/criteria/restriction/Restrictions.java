package de.ruu.lib.jpa.core.criteria.restriction;

import de.ruu.lib.jpa.core.criteria.Criterion;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/** restrictions builder */
public class Restrictions
{
	Restrictions() { }

	public static Criterion idEq(Object value)
	{
		return new IdentifierEqExpression(value);
	}
	public static Criterion eq(String property, Object value)
	{
		return new SimpleExpression(property, value, "=");
	}
	public static Criterion ne(String property, Object value)
	{
		return new SimpleExpression(property, value, "<>");
	}
	public static Criterion like(String property, Object value)
	{
		return new LikeExpression(property, value, null, false);
	}
	public static Criterion ilike(String property, Object value)
	{
		return new LikeExpression(property, value, null, true);
	}
	public static Criterion gt(String property, Object value)
	{
		return new SimpleExpression(property, value, ">");
	}
	public static Criterion lt(String property, Object value)
	{
		return new SimpleExpression(property, value, "<");
	}
	public static Criterion le(String property, Object value)
	{
		return new SimpleExpression(property, value, "<=");
	}
	public static Criterion ge(String property, Object value)
	{
		return new SimpleExpression(property, value, ">=");
	}
	public static Criterion between(String property, Object lo, Object hi)
	{
		return new BetweenExpression(property, lo, hi);
	}
	public static Criterion in(String property, Object[] values)
	{
		return new InExpression(property, values);
	}
	public static Criterion in(String property, Collection<?> values)
	{
		return new InExpression(property, values.toArray());
	}
	public static Criterion iin(String property, String[] values)
	{
		return new InExpressionInsensitive(property, values);
	}
	public static Criterion iin(String property, Collection<String> values)
	{
		return new InExpressionInsensitive(property, values.toArray(new String[values.size()]));
	}
	public static Criterion isNull(String property)
	{
		return new NullExpression(property);
	}
	public static Criterion eqProperty(String property, String otherProperty)
	{
		return new PropertyExpression(property, otherProperty, "=");
	}
	public static Criterion neProperty(String property, String otherProperty)
	{
		return new PropertyExpression(property, otherProperty, "<>");
	}
	public static Criterion ltProperty(String property, String otherProperty)
	{
		return new PropertyExpression(property, otherProperty, "<");
	}
	public static Criterion leProperty(String property, String otherProperty)
	{
		return new PropertyExpression(property, otherProperty, "<=");
	}
	public static Criterion gtProperty(String property, String otherProperty)
	{
		return new PropertyExpression(property, otherProperty, ">");
	}
	public static Criterion geProperty(String property, String otherProperty)
	{
		return new PropertyExpression(property, otherProperty, ">=");
	}
	public static Criterion isNotNull(String property)        { return new NotNullExpression(property); }
	public static Criterion and(Criterion lhs, Criterion rhs)
	{
		return new LogicalExpression(lhs, rhs, "and");
	}
	public static Criterion or(Criterion lhs, Criterion rhs)
	{
		return new LogicalExpression(lhs, rhs, "or");
	}
	public static Criterion not(Criterion expression)
	{
		return new NotExpression(expression);
	}
	public static Conjunction conjunction()
	{
		return new Conjunction();
	}
	public static Disjunction disjunction()
	{
		return new Disjunction();
	}
	public static Criterion isEmpty   (String property) { return new EmptyExpression(property); }
	public static Criterion isNotEmpty(String property)
	{
		return new NotEmptyExpression(property);
	}
	public static Criterion sizeEq(String property, long size)
	{
		return new SizeExpression(property, size, "=" );
	}
	public static Criterion sizeNe(String property, long size)
	{
		return new SizeExpression(property, size, "<>");
	}
	public static Criterion sizeGt(String property, long size)
	{
		return new SizeExpression(property, size, "<" );
	}
	public static Criterion sizeLt(String property, long size)
	{
		return new SizeExpression(property, size, ">" );
	}
	public static Criterion sizeGe(String property, long size)
	{
		return new SizeExpression(property, size, "<=");
	}
	public static Criterion sizeLe(String property, long size)
	{
		return new SizeExpression(property, size, ">=");
	}
	public static Criterion allEq(Map<?, ?> propertyValues)
	{
		Conjunction conj = conjunction();
		Iterator<?> iter = propertyValues.entrySet().iterator();
		while (iter.hasNext())
		{
			Map.Entry me = (Map.Entry) iter.next();
			conj.add(eq((String) me.getKey(), me.getValue()));
		}
		return conj;
	}
}