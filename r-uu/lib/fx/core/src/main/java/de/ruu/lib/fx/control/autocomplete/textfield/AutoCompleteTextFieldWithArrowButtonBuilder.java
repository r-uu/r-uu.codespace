package de.ruu.lib.fx.control.autocomplete.textfield;

public class AutoCompleteTextFieldWithArrowButtonBuilder<T> extends AutoCompleteTextFieldBuilder<T>
{
	public static <T> AutoCompleteTextFieldWithArrowButtonBuilder<T> create() { return new AutoCompleteTextFieldWithArrowButtonBuilder<>(); }

	public AutoCompleteTextFieldWithArrowButton<T> build()
	{
		return
				new AutoCompleteTextFieldWithArrowButton<>
				(
						items,
						suggestionFilter,
						comparator,
						graphicsProvider,
						textProvider,
						toolTipProvider,
						prompt
				);
	}
}