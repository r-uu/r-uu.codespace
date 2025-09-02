package de.ruu.lib.gen.java;

import static de.ruu.lib.gen.java.Generator.GeneratorSimple.generator;
import static de.ruu.lib.gen.java.GeneratorCompilationUnit.compilationUnit;
import static de.ruu.lib.gen.java.context.CompilationUnitContext.context;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ruu.lib.gen.GeneratorException;
import de.ruu.lib.gen.java.context.CompilationUnitContext;

class GeneratorCompilationUnitTest
{
	private CompilationUnitContext context;

	@BeforeEach void beforeEach() { context = context("package.name", "SimpleFileName"); }

	@Test void defaultCompilationUnit() throws GeneratorException
	{
		GeneratorCompilationUnit compilationUnit = compilationUnit(context, generator(context));
		
		assertThat(compilationUnit                      , is(not(nullValue())));
		assertThat(compilationUnit.generate().toString(), is(""));
	}

	@Test void defaultCompilationUnitWithSimpleGenerator() throws GeneratorException
	{
		String output = "test";

		assertThat(
				compilationUnit(
						context,
						generator(context)
								.output(output))
								.generate().toString(),
				is(output));
	}
}