package de.ruu.lib.gen.java.element.method;

import static de.ruu.lib.gen.java.Visibility.PUBLIC;
import static de.ruu.lib.gen.java.context.CompilationUnitContext.context;
import static de.ruu.lib.gen.java.doc.GeneratorJavaDoc.javaDoc;
import static de.ruu.lib.gen.java.element.GeneratorAnnotation.annotation;
import static de.ruu.lib.gen.java.element.GeneratorAnnotations.annotations;
import static de.ruu.lib.gen.java.element.GeneratorModifiers.modifiers;
import static de.ruu.lib.gen.java.element.method.GeneratorMethodInterface.interfaceMethod;
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

class GeneratorMethodInterfaceTest
{
	private CompilationUnitContext context;

	@BeforeEach void beforeEach() { context = context("package.name", "SimpleFileName"); }

	@Test void generator() throws GeneratorException
	{
		String type = "type";
		String name = "name";

		GeneratorMethodInterface generator = interfaceMethod(context, type, name);

		assertThat(generator, is(not(nullValue())));
		assertThat(generator.generate().toString(), is(type + " " + name + "();"));
	}

	@Test void generatorAnnotations() throws GeneratorException
	{
		String type       = "type";
		String name       = "name";
		String annotation = "annotation";

		GeneratorMethodInterface generator =
				interfaceMethod(context, type, name)
						.annotations(
								annotations(context)
										.add(annotation(context, annotation)));

		assertThat(generator, is(not(nullValue())));
		assertThat(
				generator.generate().toString(), is("@" + annotation + LS + type + " " + name + "();"));
	}

	@Test void generatorModifiers() throws GeneratorException
	{
		String type = "type";
		String name = "name";

		GeneratorMethodInterface generator =
				interfaceMethod(context, type, name)
						.modifiers
						(
								modifiers(context)
										.visibility(PUBLIC)
										.setFinal(true)
										.setStatic(true)
						);

		assertThat(generator, is(not(nullValue())));
		assertThat(
				generator.generate().toString(),
				is("public final static " + type + " " + name + "();"));
	}

	@Test void generatorJavaDocModifiers() throws GeneratorException
	{
		String javaDoc = "javaDoc";
		String type    = "type";
		String name    = "name";

		GeneratorMethodInterface generator =
				interfaceMethod(context, type, name)
						.childNodesSeparator(LS)
						.javaDoc
						(
								javaDoc(context)
										.add(javaDoc)
						)
						.modifiers
						(
								modifiers(context)
										.visibility(PUBLIC)
										.setFinal(true)
										.setStatic(true)
						);

		assertThat(generator, is(not(nullValue())));

		String expected =
				"""
		    /**
		     * javaDoc
		     */
		    public final static type name();""";

//		log.debug(LS + generator.generate().toString());
//		log.debug(LS + "/**" + LS + " * javaDoc" + LS + " */" + LS + "public final static " + type + " " + name + " = " + value + ";");

		assertThat(
				normaliseLineSeparator(generator.generate().toString()),
				is(normaliseLineSeparator(expected)));
	}
}