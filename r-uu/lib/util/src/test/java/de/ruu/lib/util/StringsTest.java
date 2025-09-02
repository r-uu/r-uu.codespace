package de.ruu.lib.util;

import static de.ruu.lib.util.Constants.LS;
import static de.ruu.lib.util.Strings.allTrimChars;
import static de.ruu.lib.util.Strings.lFillCharsTargetLength;
import static de.ruu.lib.util.Strings.rFillCharsTargetLength;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StringsTest
{
	@Test void testAllTrimChars__xay_xy__a               () { assertEquals("a"        , allTrimChars("xay"        , "xy"  )); }
	@Test void testAllTrimChars__abcxxxcba_bac__xxx      () { assertEquals("xxx"      , allTrimChars("abcxxxcba"  , "bac" )); }
	@Test void testAllTrimChars__abcxxxcba_x__abcxxxcba  () { assertEquals("abcxxxcba", allTrimChars("abcxxxcba"  , "x"   )); }
	@Test void testAllTrimChars__null_cab__null          () { assertEquals(null       , allTrimChars(null         , "cab" )); }
	@Test void testAllTrimChars__aaaxxxaaa_a__xxx        () { assertEquals("xxx"      , allTrimChars("aaaxxxaaa"  , "a"   )); }
	@Test void testLFillCharsTargetLength__empty_a_3__aaa() { assertEquals("aaa"      , lFillCharsTargetLength("" , 'a', 3)); }
	@Test void testLFillCharsTargetLength__x_a_4__aaax   () { assertEquals("aaax"     , lFillCharsTargetLength("x", 'a', 4)); }
	@Test void testRFillCharsTargetLength__empty_a_3_aaa () { assertEquals("aaa"      , rFillCharsTargetLength("" , 'a', 3)); }
	@Test void testRFillCharsTargetLength__x_a_4__xaaa   () { assertEquals("xaaa"     , rFillCharsTargetLength("x", 'a', 4)); }

	@Test void test()
	{
		String input  = "" + ((char) 10) + ((char) 160);
		String toTrim = "" + ((char) 10) + ((char) 160);
		assertThat(allTrimChars(input, toTrim).length(), is(0));
	}
	
	@Test void testNormalizeLineSeparator()
	{
		assertEquals(LS, Strings.normaliseLineSeparator("\n"));
		assertEquals(LS, Strings.normaliseLineSeparator("\r\n"));
		assertEquals(LS, Strings.normaliseLineSeparator(LS));
		assertEquals(LS, Strings.normaliseLineSeparator(SystemProperties.lineSeparator()));
	}
}