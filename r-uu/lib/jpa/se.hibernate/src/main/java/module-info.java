module de.ruu.lib.jpa.se.hibernate
{
	exports de.ruu.lib.jpa.se.hibernate;

	requires jakarta.persistence;
	requires jakarta.xml.bind;
	requires java.sql;
	requires static lombok;
	requires org.hibernate.orm.core;
	requires de.ruu.lib.jpa.core;
}