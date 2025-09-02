package de.ruu.lib.gen.java;

import static de.ruu.lib.gen.java.Generator.generator;
import static de.ruu.lib.gen.java.context.CompilationUnitContext.context;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ruu.lib.gen.GeneratorException;
import de.ruu.lib.gen.java.context.CompilationUnitContext;

class GeneratorTest
{
	private CompilationUnitContext context;

	@BeforeEach void beforeEach() { context = context("package.name", "SimpleFileName"); }

	@Test void defaultGenerator() throws GeneratorException
	{
		Generator generator = generator(context);
		
		assertThat(generator, is(not(nullValue())));
		assertThat(generator.generate().toString(), is(""));
		
		assertThrows(
				UnsupportedOperationException.class,
				() -> generator.add(generator),
				"expected adding generator to itself to be unsupported");
	}
}