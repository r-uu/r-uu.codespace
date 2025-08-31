package de.ruu.lib.util;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static de.ruu.lib.util.BooleanFunctions.is;
import static de.ruu.lib.util.BooleanFunctions.isNot;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BooleanFunctionsTest
{
	@Test void testIsTrue    () { assertThat(is   (true ), Matchers.is(true )); }
	@Test void testIsFalse   () { assertThat(is   (false), Matchers.is(false)); }
	@Test void testIsNotTrue () { assertThat(isNot(true ), Matchers.is(false)); }
	@Test void testIsNotFalse() { assertThat(isNot(false), Matchers.is(true )); }
}