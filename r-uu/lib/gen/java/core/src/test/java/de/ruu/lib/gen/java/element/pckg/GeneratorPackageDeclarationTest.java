package de.ruu.lib.gen.java.element.pckg;

import static de.ruu.lib.gen.java.context.CompilationUnitContext.context;
import static de.ruu.lib.gen.java.doc.GeneratorJavaDoc.javaDoc;
import static de.ruu.lib.gen.java.element.GeneratorAnnotation.annotation;
import static de.ruu.lib.gen.java.element.GeneratorAnnotations.annotations;
import static de.ruu.lib.gen.java.element.pckg.GeneratorPackageDeclaration.pckg;
import static de.ruu.lib.util.Constants.LS;
import static de.ruu.lib.util.Strings.normaliseLineSeparator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ruu.lib.gen.GeneratorException;
import de.ruu.lib.gen.java.context.CompilationUnitContext;

class GeneratorPackageDeclarationTest
{
	private CompilationUnitContext context;

	@BeforeEach void beforeEach() { context = context("package.name", "SimpleFileName"); }

	@Test void generator() throws GeneratorException
	{
		String name  = "test.package.name";

		GeneratorPackageDeclaration generator = pckg(context, name);
		
		assertThat(generator.generate().toString(), is("package " + name + ";"));
	}

	@Test void parameterisedGenerator() throws GeneratorException
	{
		String name  = "test.package.name";

		GeneratorPackageDeclaration
				generator = pckg(context, name);
		generator // pckg(context, name).annotations does not compile due to name clashes for "annotations"
				.annotations(
						annotations(context)
								.childNodesSeparator(LS)
								.add(
										annotation(context, "Annotation1"))
								.add(
										annotation(context, "Annotation2")))
				.javaDoc(
						javaDoc(context)
								.add("doc1")
								.add("doc2"));
		
		assertThat(generator, is(not(nullValue())));
		assertThat(
				normaliseLineSeparator(generator.generate().toString()),
				is(
						normaliseLineSeparator(
"""
/**
 * doc1
 * doc2
 */
@Annotation1
@Annotation2
package test.package.name;""")));
	}
}