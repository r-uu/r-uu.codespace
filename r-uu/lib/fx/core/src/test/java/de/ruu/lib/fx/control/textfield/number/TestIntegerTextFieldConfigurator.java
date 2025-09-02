package de.ruu.lib.fx.control.textfield.number;

import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.condition.OS.LINUX;

@DisabledOnOs(LINUX)
@Disabled("find out how to make these tests work")
public class TestIntegerTextFieldConfigurator
{
	private TextField textField;

	@BeforeEach public void beforeEach()
	{
		textField = new TextField();
	}

	@Test public void testDefaultConfiguratorWith0()
	{
		NumberTextFieldConfigurator.configureIntegerTextField(textField);
		textField.setText("0");
		Optional<Integer> optionalInteger = IntegerTextFieldUtility.getCurrentTextFieldValueAsInteger(textField);
		assertThat(optionalInteger.isPresent(), is(true));
		assertThat(optionalInteger.get(), is(0));
	}

	@Test public void testDefaultConfiguratorWithA()
	{
		NumberTextFieldConfigurator.configureIntegerTextField(textField);
		textField.setText("A");
		Optional<Integer> optionalInteger = IntegerTextFieldUtility.getCurrentTextFieldValueAsInteger(textField);
		assertThat(optionalInteger.isPresent(), is(false));
	}

	@Test public void testConfiguratorWithConvertAction()
	{
		NumberTextFieldConfigurator.configureIntegerTextField(textField, new DefaultNumberTextFieldPostConvertAction());
		textField.setText("A");
		Optional<Integer> optionalInteger = IntegerTextFieldUtility.getCurrentTextFieldValueAsInteger(textField);
		assertThat(optionalInteger.isPresent(), is(false));
	}
}