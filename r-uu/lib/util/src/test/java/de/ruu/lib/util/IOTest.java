package de.ruu.lib.util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

class IOTest
{
	@Test void testToString() throws Exception
	{
		String               input  = "input";
		ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
		String               output = IO.toString(stream);

		assertThat(output, is(input));
	}

	@Test void testIsListeningTrue() throws Exception
	{
		try (ServerSocket serverSocket = new ServerSocket(0))
		{
			int port = serverSocket.getLocalPort();
			assertThat(IO.isListening("localhost", port), is(true));
		}
	}

	@Test void testCapturePrintStreamOutputOfRunnableWithSystemOut()
	{
		String input  = "input";
		String output = IO.capturePrintStreamOutputOfRunnable(System.out, () ->  System.out.print(input));
		assertThat(output, containsString(input));
	}
}