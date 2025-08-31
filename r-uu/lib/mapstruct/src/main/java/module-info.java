module de.ruu.lib.mapstruct
{
	exports de.ruu.lib.mapstruct;

	requires static lombok;

	requires org.mapstruct;
	requires de.ruu.lib.util;

	opens de.ruu.lib.mapstruct to org.mapstruct;
}