package de.ruu.lib.fx.control.textfield.number;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.condition.OS.LINUX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;

@DisabledOnOs(LINUX)
class TestRegExpForBigDecimal
{
	private final Pattern p = Pattern.compile("-?\\d+(,\\d+)?");

	@Test void testInteger()
	{
		final Matcher m = p.matcher("-10");

		assertTrue(m.find());
		assertEquals("-10", m.group());
	}

	@Test void testDecimal()
	{
//		Matcher m = p.matcher("10.99");
		final Matcher m = p.matcher("10,99");

		assertTrue(m.find());
//		assertEquals("10.99", m.group());
		assertEquals("10,99", m.group());
	}
}