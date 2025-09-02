package de.ruu.app.jeeeraaah.client.rs.demo;

import de.ruu.lib.cdi.se.CDIContainer;
import jakarta.enterprise.inject.spi.CDI;

public class DBCommandRunner
{
	public static void main(String[] args)
	{
		CDIContainer.bootstrap(DBPopulate.class.getClassLoader());
		DBClean    clean    = CDI.current().select(DBClean   .class).get();
		DBPopulate populate = CDI.current().select(DBPopulate.class).get();
		clean   .run();
		populate.run();
	}
}