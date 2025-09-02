package de.ruu.lib.util.bimapped.cyclic;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class SampleBiMappingCyclicTest
{
	@Test void mapSourceToTarget()
	{
		SampleBiMappedSource source = new SampleBiMappedSource("map me");
		SampleBiMappedTarget target = source.toTarget();

		assertThat(target       , is(not(nullValue())));
		assertThat(target.name(), is(source.name()));

		SampleBiMappedSource remappedSource = target.toSource();

		assertThat(remappedSource, is(not(nullValue())));
		assertThat(remappedSource, is(source));

		SampleBiMappedTarget remappedTarget = remappedSource.toTarget();

		assertThat(remappedTarget, is(not(nullValue())));
		assertThat(remappedTarget, is(target));
	}
}