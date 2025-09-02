package de.ruu.lib.util.jira.csv.issue.hierarchy;

import static de.ruu.lib.util.jira.csv.merge.CSVFileMergedReader.HEADER_DATA_DEFAULT;
import static de.ruu.lib.util.jira.csv.merge.CSVFileMergedReader.PATH_FILE_JIRA_DATA_MERGED_DEFAULT;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;

import de.ruu.lib.util.file.BufferedFileRowsProvider;
import de.ruu.lib.util.jira.csv.issue.IssueType;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Initiative;
import de.ruu.lib.util.jira.csv.raw.CSVRecord;
import de.ruu.lib.util.jira.csv.raw.CSVRecordFactory;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroup;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroupsFactory;
import lombok.NonNull;

public interface HierarchyBuilder
{
	Hierarchy build();
	Hierarchy build(@NonNull Path csvFile, String headerData);

	static class HierarchyBuilderSimple implements HierarchyBuilder
	{
		@Override public Hierarchy build(@NonNull Path csvFile, String headerData)
		{
			BufferedFileRowsProvider bufferedFileRowsProvider = BufferedFileRowsProvider.create();

			List<String> rows = bufferedFileRowsProvider.process(csvFile, neitherBlankNorHeader(headerData));
			
			Map<String, TreeSet<CSVRecord>>            recordsByKey            = CSVRecordFactory      .recordsByKey           (rows             );
			TreeMap<String,CSVRecordGroup>             recordGroupsByKey       = CSVRecordGroupsFactory.recordGroupsByKey      (recordsByKey     );
			TreeMap<IssueType,TreeSet<CSVRecordGroup>> recordGroupsByIssueType = CSVRecordGroupsFactory.recordGroupsByIssueType(recordGroupsByKey);
			
			return Hierarchy.create(recordGroupsByIssueType);
		}

		@Override public Hierarchy build()
		{
			return build(PATH_FILE_JIRA_DATA_MERGED_DEFAULT, HEADER_DATA_DEFAULT);
		}

		private @NonNull Predicate<String> neitherBlankNorHeader(String headerData)
		{
			return string ->
			{
				if (string.isBlank())          return false;
				if (string.equals(headerData)) return false;
				return true;
			};
		}
	}

	static HierarchyBuilder create() { return new HierarchyBuilderSimple(); }

	public static void main(String[] args) throws IOException
	{
		Hierarchy hierarchy = HierarchyBuilder.create().build();
		
		Set<Initiative> initiatives = new TreeSet<>(hierarchy.initiativesByKey().values());
		
		for (Initiative initiative : initiatives)
		{
			System.out.println(initiative.lastRecord().asCSVRow());
		}
	}
}