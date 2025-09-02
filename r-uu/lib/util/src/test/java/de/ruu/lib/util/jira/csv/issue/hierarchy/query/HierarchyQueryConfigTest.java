package de.ruu.lib.util.jira.csv.issue.hierarchy.query;

import static de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryPredicates.epicHasMatchingInitiative;
import static de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryPredicates.initiativeDescriptionContains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

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
import de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryConfig.PredicateEpic;
import de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryConfig.PredicateInitiative;
import de.ruu.lib.util.jira.csv.merge.CSVFileMergedReader;
import de.ruu.lib.util.jira.csv.raw.CSVRecord;
import de.ruu.lib.util.jira.csv.raw.CSVRecordFactory;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroup;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroupsFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Disabled
class HierarchyQueryConfigTest
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

	private Hierarchy            hierarchy = Hierarchy           .create(recordGroupsByIssueType);
	private HierarchyQueryConfig config    = HierarchyQueryConfig.create();

	@Disabled
	@Test void initiativesOnly()
	{
		config.predicateInitiative(initiativeDescriptionContains("Migration"));

		HierarchyQuery  query  = HierarchyQuery.create();
		Set<Initiative> result = query.query(hierarchy, config);
		
		for (Initiative initiative : result)
		{
			log.debug(initiative.lastRecord().asCSVRow());
			assertThat(initiative.epics().size(), is(0));
		}
	}

	@Disabled
	@Test void initiativesWithEpics()
	{
		config.predicateInitiative(initiativeDescriptionContains("Migration"));
		config.predicateEpic      (e -> true);

		HierarchyQuery  query  = HierarchyQuery.create();
		Set<Initiative> result = query.query(hierarchy, config);
		
		for (Initiative initiative : result)
		{
			log.debug(initiative.lastRecord().asCSVRow());
			assertThat(initiative.epics().size(), is(greaterThan(0)));
		}
	}

	@Disabled
	@Test void epicsWithTheirInitiatives()
	{
		PredicateInitiative predicateInitiative = initiativeDescriptionContains("Migration");
		PredicateEpic       predicateEpic       = epicHasMatchingInitiative(predicateInitiative);

		config.predicateEpic      (predicateEpic      );
		config.predicateInitiative(predicateInitiative);

		HierarchyQuery  query  = HierarchyQuery.create();
		Set<Initiative> result = query.query(hierarchy, config);
		
		for (Initiative initiative : result)
		{
			log.debug(initiative.lastRecord().asCSVRow());
			assertThat(initiative.epics().size(), is(greaterThan(0)));
		}
	}
}