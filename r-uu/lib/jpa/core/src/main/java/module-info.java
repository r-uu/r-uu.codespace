module de.ruu.lib.jpa.core
{
	exports de.ruu.lib.jpa.core;

	requires com.fasterxml.jackson.annotation;
	requires de.ruu.lib.util;
	requires jakarta.annotation;
	requires jakarta.json.bind;
	requires jakarta.persistence;
	requires java.sql;
	requires static lombok;
	requires java.desktop;

	opens de.ruu.lib.jpa.core to org.hibernate.orm.core;
}