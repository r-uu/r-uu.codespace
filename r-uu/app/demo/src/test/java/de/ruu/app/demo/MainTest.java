package de.ruu.app.demo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class MainTest {

	// Store the original System.out to restore after the test
	private final PrintStream originalOut = System.out;

	// ByteArrayOutputStream to capture the output of System.out
	private ByteArrayOutputStream outContent;

	@BeforeEach
	void setUpStreams() {
		// Redirect System.out to capture printed content
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	@AfterEach
	void restoreStreams() {
		// Restore original System.out to avoid side effects
		System.setOut(originalOut);
	}

	@Test
	void testMainPrintsHelloWorld() {
		// Execute the main method
		Main.main(new String[]{});

		// Capture and trim the output
		String output = outContent.toString().trim();

		// Use Hamcrest matcher for assertion
		assertThat("Main should print 'Hello world!'", output, equalTo("Hello world!"));
	}
}
