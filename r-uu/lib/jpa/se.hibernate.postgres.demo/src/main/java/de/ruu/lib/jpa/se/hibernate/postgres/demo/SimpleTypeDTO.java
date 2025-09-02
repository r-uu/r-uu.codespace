package de.ruu.lib.jpa.se.hibernate.postgres.demo;

import de.ruu.lib.jpa.core.AbstractDTO;

class SimpleTypeDTO extends AbstractDTO<SimpleTypeEntity> implements SimpleType
{
	private String name;

	SimpleTypeDTO(String name) { this.name = name; }

	@Override public String name() { return name; }

	public SimpleTypeEntity toSource()
	{
		return new SimpleTypeEntity(name);
	}
}