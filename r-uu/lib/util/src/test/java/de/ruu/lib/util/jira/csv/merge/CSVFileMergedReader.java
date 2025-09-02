package de.ruu.lib.util.jira.csv.merge;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import de.ruu.lib.util.file.BufferedFileRowsProvider;
import lombok.NonNull;

/**
 * Provides all lines in a text file as a {@link List} of {@link String}s that match the {@link #neitherBlankNorHeader()}
 * predicate.  
 */
public class CSVFileMergedReader
{
	private final static Config CONFIG = ConfigProvider.getConfig();

	private final static String CONFIG_KEY_HEADER_DATA                = "header.data.csv";
	private final static String CONFIG_KEY_PATH_FILE_JIRA_DATA_MERGED = "path.file.data.jira.merge";

	public  final static String HEADER_DATA_DEFAULT                   =
			CONFIG.getValue(CONFIG_KEY_HEADER_DATA, String.class);
	public  final static Path   PATH_FILE_JIRA_DATA_MERGED_DEFAULT    =
			Path.of(CONFIG.getValue(CONFIG_KEY_PATH_FILE_JIRA_DATA_MERGED, String.class));

	private Path pathFileDataJiraMerge;

	public CSVFileMergedReader() { this(CONFIG_KEY_PATH_FILE_JIRA_DATA_MERGED); }

	public CSVFileMergedReader(@NonNull String configKey)
	{
		pathFileDataJiraMerge = Paths.get(CONFIG.getValue(configKey, String.class));
	}

	/** @return all rows from file in {@link #EXCEL_EXPORT_MERGED} that are neither blank nor {@link #HEADER} */
	public List<String> csvDataRows()
	{
		BufferedFileRowsProvider bufferedFileRowsProvider = BufferedFileRowsProvider.create();

		return bufferedFileRowsProvider.process(pathFileDataJiraMerge, neitherBlankNorHeader());
	}

	public static CSVFileMergedReader create(@NonNull String configKey) { return new CSVFileMergedReader(configKey); }
	public static CSVFileMergedReader create(                         ) { return new CSVFileMergedReader(); }

	private @NonNull Predicate<String> neitherBlankNorHeader()
	{
		return string ->
		{
			if (string.isBlank())           return false;
			if (string.equals(CONFIG_KEY_HEADER_DATA)) return false;
			return true;
		};
	}
}