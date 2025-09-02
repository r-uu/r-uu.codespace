package de.ruu.lib.jpa.core.mapstruct;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class TestMapstruct
{
	@Test void test()
	{
		SimpleMappedEntity source   = new SimpleMappedEntity("name");
		SimpleMappedDTO    target   = Mapper.INSTANCE.map(source); //    mapping
		SimpleMappedEntity remapped = target.toSource();           // re-mapping
		isEquals(target, source  );
		isEquals(target, remapped);
	}

	private void isEquals(SimpleMappedDTO source, SimpleMappedEntity target)
	{
		assertThat(source.name(), is(target.name()));
	}
}