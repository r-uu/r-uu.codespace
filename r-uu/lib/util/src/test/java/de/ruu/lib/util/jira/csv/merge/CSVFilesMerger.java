package de.ruu.lib.util.jira.csv.merge;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import de.ruu.lib.util.csv.CSVDirectoryProcessor;
import de.ruu.lib.util.csv.CSVDirectoryProcessor.CSVDirectoryProcessorSimple;
import de.ruu.lib.util.csv.CSVFileProcessor;
import de.ruu.lib.util.csv.CSVFileProcessor.CSVFileProcessorSimple;
import de.ruu.lib.util.file.BufferedFileRowsProvider;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

public interface CSVFilesMerger
{
	/**
	 * @param sourceDirectory where the csv files to be merged live
	 * @param targetDirectory where the merged csv file is stored
	 * @throws IOException if merge process fails due to io problems
	 */
	void merge(@NonNull Path sourceDirectory, @NonNull Path targetDirectory) throws IOException;

	class CSVFilesMergerSimple implements CSVFilesMerger
	{
		private final static Config CONFIG = ConfigProvider.getConfig();

		private final static String HEADER_DATA_CSV = CONFIG.getValue("header.data.csv", String.class);

		private final static String DATE_STRING_PATTERN_FILENAME_PREFIX = CONFIG.getValue("date.string.pattern.filename.prefix", String.class);
		private final static String DATE_STRING_PATTERN_CELLVALUE       = CONFIG.getValue("date.string.pattern.cellvalue"      , String.class);

		private final static DateTimeFormatter FORMATTER_FILENAME_TO_DATE = DateTimeFormatter.ofPattern(DATE_STRING_PATTERN_FILENAME_PREFIX);
		private final static DateTimeFormatter FORMATTER_DATE_TO_STRING   = DateTimeFormatter.ofPattern(DATE_STRING_PATTERN_CELLVALUE);

		@RequiredArgsConstructor
		@Slf4j
		private static class CSVFileProcessorLocal extends CSVFileProcessorSimple
		{
			private final @NonNull Writer writer;

			@Override public void process(@NonNull Path csvFile)
			{
				log.debug("processing file {}", csvFile);

				try
				{
					writer.write(HEADER_DATA_CSV + "\n");

					LocalDate date                      = extractDate(csvFile);
					String    dateAsStringWithSeparator = FORMATTER_DATE_TO_STRING.format(date) + ";";

					BufferedFileRowsProvider fileRowsProvider = BufferedFileRowsProvider.create();

					boolean firstLine = true;
					for (String row : fileRowsProvider.process(csvFile, neitherBlankNorHeader()))
					{
						if (firstLine)
						{
							firstLine = false;
						}
						else
						{
							writer.write(dateAsStringWithSeparator + row + "\n");
						}
					}
				}
				catch (IOException e)
				{
					throw new RuntimeException("failure writing to file", e);
				}

				log.debug("processed  file {}", csvFile);
			}

			/** assumes that filename starts with DATE_STRING_PATTERN_FILENAME_PREFIX */
			private LocalDate extractDate(Path path)
			{
				return
						LocalDate.parse
						(
								path
										.getFileName()
										.toString()
										.substring(0, DATE_STRING_PATTERN_FILENAME_PREFIX.length()),
								FORMATTER_FILENAME_TO_DATE
						);
			}

			private @NonNull Predicate<String> neitherBlankNorHeader()
			{
				return string ->
				{
					if (string.isBlank())               return false;
					if (string.equals(HEADER_DATA_CSV)) return false;
					return true;
				};
			}
		}
		
		@RequiredArgsConstructor
		@Slf4j
		private static class CSVDirectoryProcessorLocal extends CSVDirectoryProcessorSimple
		{
			private final @NonNull Path targetFile;

			@Override public void process(@NonNull Path csvDirectory)
			{
				String absoluteTargetPathAsString = targetFile.toAbsolutePath().normalize().toString();

				try (Writer writer = new BufferedWriter(new FileWriter(absoluteTargetPathAsString, StandardCharsets.UTF_8, false)))
				{
					CSVFileProcessor processor = new CSVFileProcessorLocal(writer);
					try (Stream<Path> paths = Files.list(csvDirectory))
					{
						log.debug("processing directory {}", csvDirectory);
						paths.forEach(path -> processor.process(path));
						log.debug("processed  directory {}", csvDirectory);
					}
					catch (IOException e)
					{
						throw new RuntimeException("failure listing files in " + csvDirectory, e);
					}
				}
				catch (IOException e)
				{
					throw new RuntimeException("failure writing to " + absoluteTargetPathAsString, e);
				}
			}
			
			private static CSVDirectoryProcessor create(@NonNull Path targetFile)
			{
				return new CSVDirectoryProcessorLocal(targetFile);
			}
		}

		@Override public void merge(@NonNull Path sourceDirectory, @NonNull Path targetFile) throws IOException
		{
			CSVDirectoryProcessor processor = CSVDirectoryProcessorLocal.create(targetFile);
			processor.process(sourceDirectory);
		}
	}

	static CSVFilesMerger create() { return new CSVFilesMergerSimple(); }
	
	public static void main(String[] args) throws IOException
	{
		Config CONFIG = ConfigProvider.getConfig();

		Path sourceDirectory = Paths.get(CONFIG.getValue("path.directory.data.jira.transform.csv.work", String.class));
		Path targetFile      = Paths.get(CONFIG.getValue("path.file.data.jira.merge"                  , String.class));
		
		create().merge(sourceDirectory, targetFile);
	}
}