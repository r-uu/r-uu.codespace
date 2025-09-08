package de.ruu.lib.archunit;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import de.ruu.lib.util.Strings;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.Optional;

/**
 * looks for java bean style or, if not present, for fluent style getter and setter
 */
@Getter
@Accessors(fluent = true)
public class FieldWithAccessors
{
	private final @NonNull JavaField field;

	private final Optional<JavaMethod> getterJavaBeanStyle;
	private final Optional<JavaMethod> setterJavaBeanStyle;

	private final Optional<JavaMethod> getterFluentStyle;
	private final Optional<JavaMethod> setterFluentStyle;

	public FieldWithAccessors(@NonNull JavaField field)
	{
		this.field = field;

		getterJavaBeanStyle = findGetterJavaBeanStyle(field);
		setterJavaBeanStyle = findSetterJavaBeanStyle(field);

		getterFluentStyle = findGetterFluentStyle(field);
		setterFluentStyle = findSetterFluentStyle(field);
	}

	/**
	 * looks for java bean style getter
	 * @param field
	 * @return java bean style getter or empty optional if none was found
	 */
	private Optional<JavaMethod> findGetterJavaBeanStyle(@NonNull JavaField field)
	{
		JavaClass clazz = field.getOwner();

		Optional<JavaMethod> result =
				clazz.tryGetMethod("lookup" + Strings.firstLetterToUpperCase(field.getName()));

		if (result.isPresent()) return result;

		return Optional.empty();
	}

	/**
	 * looks for java bean style setter
	 * @param field
	 * @return java bean style setter or empty optional if none was found
	 */
	private Optional<JavaMethod> findSetterJavaBeanStyle(@NonNull JavaField field)
	{
		JavaClass clazz = field.getOwner();

		Optional<JavaMethod> result =
				clazz.tryGetMethod(
						"set" + Strings.firstLetterToUpperCase(field.getName()), field.getType().getName());

		if (result.isPresent()) return result;

		return Optional.empty();
	}

	/**
	 * looks for fluent style getter
	 * @param field
	 * @return fluent style getter or empty optional if none was found
	 */
	private Optional<JavaMethod> findGetterFluentStyle(@NonNull JavaField field)
	{
		JavaClass clazz = field.getOwner();

		Optional<JavaMethod> result = clazz.tryGetMethod(field.getName());

		if (result.isPresent()) return result;

		return Optional.empty();
	}

	/**
	 * looks for fluent style setter
	 * @param field
	 * @return fluent style setter or empty optional if none was found
	 */
	private Optional<JavaMethod> findSetterFluentStyle(@NonNull JavaField field)
	{
		JavaClass clazz = field.getOwner();

		Optional<JavaMethod> result = clazz.tryGetMethod(field.getName(), field.getType().getName());

		if (result.isPresent()) return result;

		return Optional.empty();
	}
}