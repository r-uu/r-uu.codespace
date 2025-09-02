package de.ruu.lib.gen.java;

import static de.ruu.lib.gen.java.Generator.GeneratorSimple.generator;
import static de.ruu.lib.gen.java.GeneratorCodeBlock.codeBlokk;
import static de.ruu.lib.gen.java.context.CompilationUnitContext.context;
import static de.ruu.lib.util.Constants.LS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ruu.lib.gen.GeneratorException;
import de.ruu.lib.gen.java.context.CompilationUnitContext;

class GeneratorCodeBlockTest
{
	private CompilationUnitContext context;

	@BeforeEach void beforeEach() { context = context("package.name", "SimpleFileName"); }

	@Test void defaultCompilationUnit() throws GeneratorException
	{
		GeneratorCodeBlock codeBlock = codeBlokk(context);
		
		assertThat(codeBlock                      , is(not(nullValue())));
		assertThat(codeBlock.generate().toString(), is("{" + LS + "}"));
	}

	@Test void defaultCompilationUnitWithSimpleGenerator() throws GeneratorException
	{
		String content = "content";
		GeneratorCodeBlock codeBlock = codeBlokk(context);
		codeBlock.add(generator(context).output(content));

		assertThat(
				codeBlokk(context)
						.add
						(
								generator(context)
										.output(content)
						)
						.generate().toString(),
				is("{" + LS + "\t" + content + LS + "}"));
	}
}