package de.ruu.lib.fx.control.autocomplete;

import static de.ruu.lib.fx.control.autocomplete.BiPredicateCountrySuggestion.BIPREDICATE_COUNTRY_SUGGESTION;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class BiPredicateCountrySuggestionTest
{
	@Test public void test()
	{
		final Country country = Country.countries().get(0);
		assertTrue(BIPREDICATE_COUNTRY_SUGGESTION.test(country, country.getName()));
	}
}