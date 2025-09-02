package de.ruu.lib.util.jira.csv.issue.hierarchy.query;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.ruu.lib.util.jira.csv.issue.IssueType;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Initiative;
import de.ruu.lib.util.jira.csv.issue.hierarchy.Hierarchy;
import de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryConfigs.InitiativesInReportPlanned;
import de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryConfigs.StoriesWithEffortConsumed;
import de.ruu.lib.util.jira.csv.merge.CSVFileMergedReader;
import de.ruu.lib.util.jira.csv.raw.CSVRecord;
import de.ruu.lib.util.jira.csv.raw.CSVRecordFactory;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroup;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroupsFactory;

@Disabled
class HierarchyQueryReportTest
{
	// read csv data rows exported by excel and merged by CSVFilesMerger
	private List<String> csvDataRows = CSVFileMergedReader.create("path.file.data.jira.merge").csvDataRows();

	// make csv records from csv data rows and put them in a map by key
	private Map    <String   , TreeSet<CSVRecord>>      recordsByKey            =
			CSVRecordFactory      .recordsByKey     (csvDataRows );
	private TreeMap<String   , CSVRecordGroup>          recordGroupsByKey       =
			CSVRecordGroupsFactory.recordGroupsByKey(recordsByKey);

	// make csv record groups from csv records and put them in a map by issue type
	private TreeMap<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType =
			CSVRecordGroupsFactory.recordGroupsByIssueType(recordGroupsByKey);

	private Hierarchy hierarchy = Hierarchy.create(recordGroupsByIssueType);

	@Test void storiesWithEffortConsumed() throws IOException
	{
		HierarchyQuery  query  = HierarchyQuery.create();
		Set<Initiative> result = query.query(hierarchy, new StoriesWithEffortConsumed());

		HierarchyQueryReport report = HierarchyQueryReports.storiesWithEffortConsumed();
		report.report(result);
	}

	@Test void initiativesInReportPlanned() throws IOException
	{
		HierarchyQuery  query  = HierarchyQuery.create();
		Set<Initiative> result = query.query(hierarchy, new InitiativesInReportPlanned());

		HierarchyQueryReport report = HierarchyQueryReports.initiativesInReportPlanned();
		report.report(result);
	}
}