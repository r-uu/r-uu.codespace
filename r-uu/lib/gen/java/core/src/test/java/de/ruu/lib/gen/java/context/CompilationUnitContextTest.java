package de.ruu.lib.gen.java.context;

import static de.ruu.lib.gen.java.Generator.generator;
import static de.ruu.lib.gen.java.context.CompilationUnitContext.context;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import de.ruu.lib.gen.java.Generator;

class CompilationUnitContextTest
{
	@Test void parameterisedContext()
	{
		String packageName    = "package.name";
		String simpleFileName = "SimpleFileName";

		CompilationUnitContext context = context(packageName, simpleFileName);

		assertThat(context, is(not(nullValue())));

		assertThat(context.packageName()   .toString(), is(packageName));
		assertThat(context.simpleFileName().toString(), is(simpleFileName));

		assertThat(context.registeredGenerators(), is(not(nullValue())));
		assertThat(context.registeredGenerators(), is(Collections.emptyList()));

		assertThat(context.importManager().targetCompilationUnitPackageName(), is(packageName));
		assertThat(context.importManager().targetCompilationUnitSimpleName() , is(simpleFileName));

		Generator generator = generator(context);
		assertThat(context.registeredGenerators().size(), is(0)); // generator was not registered

		context.register(generator);
		assertThat(context.registeredGenerators().size(), is(1)); // generator was     registered once

		assertThrows(
				UnsupportedOperationException.class,
				() -> context.register(generator),
				"expected adding generator more than once to be unsupported");
	}
}