package de.ruu.lib.jpa.core.criteria.restriction;

/** Empty expression */
public class EmptyExpression extends AbstractEmptynessExpression
{
	protected EmptyExpression(String property)
	{
		super(property);
	}

	@Override protected boolean excludeEmpty()
	{
		return false;
	}
}