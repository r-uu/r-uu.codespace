package de.ruu.lib.util.bimapped.simple;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class SampleBiMappingTest
{
	@Test void mapSourceToTarget()
	{
		SampleBiMappedSource source = new SampleBiMappedSource("map me");
		SampleBiMappedTarget target;

		Optional<SampleBiMappedTarget> optionalTarget = source.map(SampleBiMappedTarget.class);

		if (optionalTarget.isPresent())
		{
			target = optionalTarget.get();
		}
		else
		{
			target = new SampleBiMappedTarget(source);
		}

		assertThat(target       , is(not(nullValue())));
		assertThat(target.name(), is(source.name()));

		SampleBiMappedSource remapped = null;

		Optional<SampleBiMappedSource> optionalSource = target.map(SampleBiMappedSource.class);

		if (optionalSource.isPresent())
		{
			remapped = optionalSource.get();
		}

		assertThat(remapped, is(not(nullValue())));
		assertThat(remapped, is(source));
	}
}