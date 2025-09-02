module de.ruu.lib.jpa.se.eclipselink
{
	exports de.ruu.lib.jpa.se.eclipselink;

	requires static lombok;
	requires jakarta.persistence;
	requires org.slf4j;
	requires eclipselink;
	requires de.ruu.lib.jpa.core;
}