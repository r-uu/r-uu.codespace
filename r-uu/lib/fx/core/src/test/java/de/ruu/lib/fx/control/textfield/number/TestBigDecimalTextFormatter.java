package de.ruu.lib.fx.control.textfield.number;

import javafx.scene.control.TextField;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.condition.OS.LINUX;

@DisabledOnOs(LINUX)
@Disabled("find out how to make these tests work")
public class TestBigDecimalTextFormatter
{
	@Test public void testInteger()
	{
		final Integer integer = 10;
		final TextField textField = new TextField();
		textField.setTextFormatter(BigDecimalTextFormatter.formatter());
		textField.setText(integer.toString());
		assertEquals(integer, Integer.valueOf(textField.getText()));
	}

	@Test public void testIntegerNegative()
	{
		final Integer integer = -10;
		final TextField textField = new TextField();
		textField.setTextFormatter(BigDecimalTextFormatter.formatter());
		textField.setText(integer.toString());
		assertEquals(integer, Integer.valueOf(textField.getText()));
	}

	@Test public void testBigDecimal()
	{
		final BigDecimal bigDecimal = BigDecimal.valueOf(10.0);
		final TextField textField = new TextField();
		textField.setTextFormatter(BigDecimalTextFormatter.formatter());
		textField.setText(bigDecimal.toString());
		assertEquals(bigDecimal, new BigDecimal(textField.getText()));
	}

	@Test public void testBigDecimalNegative()
	{
		final BigDecimal bigDecimal = BigDecimal.valueOf(-10.0);
		final TextField textField = new TextField();
		textField.setTextFormatter(BigDecimalTextFormatter.formatter());
		textField.setText(bigDecimal.toString());
		assertEquals(bigDecimal, new BigDecimal(textField.getText()));
	}
}