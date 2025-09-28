module de.ruu.lib.jpa.core.mapstruct
{
	exports de.ruu.lib.jpa.core.mapstruct;

	requires static lombok;

	requires org.slf4j;
	requires org.mapstruct;

	requires de.ruu.lib.jpa.core;
	requires de.ruu.lib.mapstruct;

	opens de.ruu.lib.jpa.core.mapstruct to org.mapstruct;
}