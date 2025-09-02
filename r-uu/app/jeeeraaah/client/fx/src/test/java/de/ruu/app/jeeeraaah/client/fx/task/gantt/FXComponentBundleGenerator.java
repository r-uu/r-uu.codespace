package de.ruu.app.jeeeraaah.client.fx.task.gantt;

import de.ruu.lib.gen.GeneratorException;
import de.ruu.lib.gen.java.fx.comp.GeneratorFXCompBundle;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class FXComponentBundleGenerator
{
	public static void main(String[] args) throws IOException, GeneratorException
	{
		String packageName   = FXComponentBundleGenerator.class.getPackageName();
		String componentName = "TaskTreeTableView";
		GeneratorFXCompBundle generator;

		log.debug("creating java fx component bundle for java fx bean model {}", componentName);
		generator =
				new GeneratorFXCompBundle
						(
								packageName,
								componentName
						);
		generator.run();
		log.debug("created  java fx component bundle for java fx bean model {}", componentName);
	}
}