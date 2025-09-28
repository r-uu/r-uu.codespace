module de.ruu.lib.jpa.se.hibernate
{
	exports de.ruu.lib.jpa.se.hibernate;

	requires de.ruu.lib.jpa.core;
	requires jakarta.persistence;
	requires jakarta.xml.bind;
	requires java.sql;
	requires org.hibernate.orm.core;
	requires static lombok;
	requires org.slf4j;
}