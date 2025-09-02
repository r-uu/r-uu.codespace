package de.ruu.lib.postgres;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public abstract class PostgresUtil
{
	public static void backup(
			Path executable, String host, int port, String dbName, String username, String password, Path backupFile)
			throws IOException, InterruptedException
	{
		List<String> command = List.of
		(
				executable.toString(),
				"-h", host,
				"-p", String.valueOf(port),
				"-U", username,
				"-F", "c", // custom format
				"-f", backupFile.toAbsolutePath().toString(),
				dbName
		);

		runCommandWithEnv(command, Map.of("PGPASSWORD", password));
	}

	public static void restore(
			Path executable, String host, int port, String dbName, String username, String password, Path backupFile)
			throws IOException, InterruptedException
	{
		List<String> command = List.of
		(
				executable.toString(),
				"-h", host,
				"-p", String.valueOf(port),
				"-U", username,
				"-d", dbName,
				"-c",  // clean (drop objects before recreating)
				backupFile.toAbsolutePath().toString()
		);

		runCommandWithEnv(command, Map.of("PGPASSWORD", password));
	}

	private static void runCommandWithEnv(List<String> command, Map<String, String> env)
			throws IOException, InterruptedException
	{
		ProcessBuilder builder = new ProcessBuilder(command);
		builder.environment().putAll(env);
		builder.redirectErrorStream(true);
		Process process = builder.start();
		// Log output
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())))
		{
			reader.lines().forEach(System.out::println);
		}
		int exitCode = process.waitFor();
		if (exitCode != 0) throw new IOException("command failed with exit code " + exitCode);
	}
}