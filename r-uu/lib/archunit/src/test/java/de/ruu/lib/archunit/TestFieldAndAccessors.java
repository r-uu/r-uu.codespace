package de.ruu.lib.archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class TestFieldAndAccessors
{
	@Getter
	@Setter
	@Accessors(fluent = true)
	private static class ClassWithFluentAccessorsOnly
	{
		private boolean booleanField;
	}

	@Test void testFieldsAndAccessors()
	{
		JavaClass clazz = new ClassFileImporter().importClass(ClassWithFluentAccessorsOnly.class);

		FieldAndAccessors fieldAndAccessors =
				new FieldAndAccessors(clazz.getAllFields().iterator().next());

		assertThat(fieldAndAccessors.getterJavaBeanStyle().isPresent(), is(false));
		assertThat(fieldAndAccessors.setterJavaBeanStyle().isPresent(), is(false));

		assertThat(fieldAndAccessors.getterFluentStyle().isPresent(), is(true));
		assertThat(fieldAndAccessors.setterFluentStyle().isPresent(), is(true));
	}
}