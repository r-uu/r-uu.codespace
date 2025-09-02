package de.ruu.lib.gen.java.element.pckg;

import static de.ruu.lib.gen.java.context.CompilationUnitContext.context;
import static de.ruu.lib.gen.java.element.pckg.GeneratorPackageStatement.pckgStatement;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ruu.lib.gen.GeneratorException;
import de.ruu.lib.gen.java.context.CompilationUnitContext;

class GeneratorPackageStatementTest
{
	private CompilationUnitContext context;

	@BeforeEach void beforeEach() { context = context("package.name", "SimpleFileName"); }

	@Test void parameterisedGenerator() throws GeneratorException
	{
		String name  = "test.package.name";

		GeneratorPackageStatement generator = pckgStatement(context, name);
		
		assertThat(generator, is(not(nullValue())));
		assertThat(generator.generate().toString(), is("package " + name + ";"));
	}
}