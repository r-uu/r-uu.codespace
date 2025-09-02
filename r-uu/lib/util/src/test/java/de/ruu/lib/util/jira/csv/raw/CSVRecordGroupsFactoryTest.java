package de.ruu.lib.util.jira.csv.raw;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import de.ruu.lib.util.jira.csv.issue.IssueType;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
class CSVRecordGroupsFactoryTest
{
	private final List<String> csvDataRows =
			List.of
			(
// two issue types (story, subtask) with two issue groups (for each key) containing two issues each (for each day)
  "2024.04.01;Version 2.2.1;RENEW-47891;Story;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
, "2024.04.02;Version 2.2.1;RENEW-47891;Story;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
, "2024.04.01;Version 2.2.1;RENEW-47893;Story;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
, "2024.04.02;Version 2.2.1;RENEW-47893;Story;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
, "2024.04.01;Version 2.2.1;RENEW-47892;Unteraufgabe;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
, "2024.04.02;Version 2.2.1;RENEW-47892;Unteraufgabe;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
, "2024.04.01;Version 2.2.1;RENEW-47894;Unteraufgabe;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
, "2024.04.02;Version 2.2.1;RENEW-47894;Unteraufgabe;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
			);

	@Test void testRecordGroupsByIssueType()
	{
		Map<String,TreeSet<CSVRecord>>  recordsByKey      = CSVRecordFactory      .recordsByKey     (csvDataRows);
		TreeMap<String, CSVRecordGroup> recordGroupsByKey = CSVRecordGroupsFactory.recordGroupsByKey(recordsByKey);

		TreeMap<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType =
				CSVRecordGroupsFactory.recordGroupsByIssueType(recordGroupsByKey);

		assertThat(recordGroupsByIssueType.size(), Matchers.is(IssueType.values().length));

		for (IssueType issueType : recordGroupsByIssueType.keySet())
		{
			TreeSet<CSVRecordGroup> recordGroups = recordGroupsByIssueType.get(issueType);

			log.debug("record groups container for issue type {} contains {} record groups", issueType, recordGroups.size());

//			assertThat(recordGroups.size(), Matchers.is(2));
			
			for (CSVRecordGroup csvRecordGroup : recordGroups)
			{
				log.debug("testing record group with key {}", csvRecordGroup.key());
				if (csvRecordGroup.records().size() > 0)
				{
					assertThat(csvRecordGroup.records().size(), Matchers.is(2));
				}
			}
		}
	}
}