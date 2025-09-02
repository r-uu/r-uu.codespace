package de.ruu.lib.gen.java.doc;

import static de.ruu.lib.gen.java.context.CompilationUnitContext.context;
import static de.ruu.lib.gen.java.doc.GeneratorJavaDoc.javaDoc;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ruu.lib.gen.GeneratorException;
import de.ruu.lib.gen.java.context.CompilationUnitContext;

class GeneratorJavaDocTest
{
	private CompilationUnitContext context;

	@BeforeEach void beforeEach() { context = context("package.name", "SimpleFileName"); }

	@Test void defaultGenerator() throws GeneratorException
	{
		GeneratorJavaDoc generator = javaDoc(context);

		assertThat(generator, is(not(nullValue())));
		assertThat(generator.generate().toString(), is(""));
	}

	@Test void defaultGeneratorWithSingleLine() throws GeneratorException
	{
		String line = "line";

		assertThat(
				javaDoc(context)
						.add(line)
						.generate().toString(),
				is("/**" + System.lineSeparator() + " * " + line + System.lineSeparator() + " */"));
	}
}