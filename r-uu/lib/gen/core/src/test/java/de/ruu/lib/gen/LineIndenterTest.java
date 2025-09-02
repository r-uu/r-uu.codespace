package de.ruu.lib.gen;

import org.junit.jupiter.api.Test;

import static de.ruu.lib.util.Constants.LS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LineIndenterTest
{
	@Test void testIndentString()
	{
		// should not indent because indent level is 0 and indenting is false
		LineIndenter lineIndenter = new LineIndenter();

		String noLines = "noLines";

		assertThat(lineIndenter.indent(noLines), is(noLines));

		// should not indent because indentation is ""
		lineIndenter.setIndentationLevel(1);

		assertThat(lineIndenter.indent(noLines), is(noLines));

		// should indent because indentation is "x"
		lineIndenter.setIndentation("x");

		assertThat(lineIndenter.indent(noLines), is("x" + noLines));
	}

	@Test void testIndentStringBuffer()
	{
		// should not indent because indent level is 0 and indenting is false
		LineIndenter lineIndenter = new LineIndenter();

		String noLines = "noLines";

		assertThat(lineIndenter.indent(noLines).toString(), is(noLines));

		// should not indent because indentation is ""
		lineIndenter.setIndentationLevel(1);

		assertThat(lineIndenter.indent(noLines).toString(), is(noLines));

		// should indent because indentation is "x"
		lineIndenter.setIndentation("x");

		assertThat(lineIndenter.indent(noLines).toString(), is("x" + noLines));
	}

	@Test void testMultiLines1()
	{
		String multiLines =    "1" + LS +    "2" + LS +    "3";
		String expected   = "xxx1" + LS + "xxx2" + LS + "xxx3";

		LineIndenter lineIndenter = new LineIndenter("x", 3);

		assertEquals(expected, lineIndenter.indent(multiLines));
	}

	@Test void testMultiLines2()
	{
		String multiLines =
				"""
				1
				2
				3""";

		String expected =
				"""
				xxx1
				xxx2
				xxx3""";

		LineIndenter lineIndenter = new LineIndenter("x", 3);

		assertEquals(expected, lineIndenter.indent(multiLines));
	}
}