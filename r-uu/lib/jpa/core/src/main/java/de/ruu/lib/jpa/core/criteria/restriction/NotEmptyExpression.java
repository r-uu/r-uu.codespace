package de.ruu.lib.jpa.core.criteria.restriction;

/** not empty expression */
public class NotEmptyExpression extends AbstractEmptynessExpression
{
	protected NotEmptyExpression(String property)
	{
		super(property);
	}

	@Override protected boolean excludeEmpty()
	{
		return true;
	}
}