package de.ruu.lib.util.bimapped;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;

import java.util.Optional;

class BiMapTest
{
	private class Source { }
	private class Target { }

	@Test void test()
	{
		BiMap biMap = new BiMap();

		Source source = new Source();
		Target target = new Target();

		biMap.put(source, target);

		Optional<Target> optionalTarget = biMap.lookup(source, Target.class);
		Optional<Source> optionalSource = biMap.lookup(target, Source.class);

		assertThat(optionalTarget, is(not(nullValue())));
		assertThat(optionalSource, is(not(nullValue())));

		assertThat(optionalTarget.isPresent(), is(true));
		assertThat(optionalSource.isPresent(), is(true));

		assertThat(optionalTarget.get(), is(target));
		assertThat(optionalSource.get(), is(source));
	}
}