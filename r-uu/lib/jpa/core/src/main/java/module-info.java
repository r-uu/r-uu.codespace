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

	// Öffnung für Reflection ohne harte Bindung an ein bestimmtes Modul, z. B. Hibernate
	opens de.ruu.lib.jpa.core;
}