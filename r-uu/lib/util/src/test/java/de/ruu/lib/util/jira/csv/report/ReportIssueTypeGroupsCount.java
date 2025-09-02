package de.ruu.lib.util.jira.csv.report;

import java.io.IOException;
import java.util.Map;
import java.util.TreeSet;

import de.ruu.lib.util.jira.csv.issue.IssueType;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroup;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportIssueTypeGroupsCount
{
	@NonNull private Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType;

	private ReportIssueTypeGroupsCount(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		this.recordGroupsByIssueType = recordGroupsByIssueType;
	}

	public void report() throws IOException
	{
		int countAllRecords = 0;

		for (IssueType issueType : recordGroupsByIssueType.keySet())
		{
			TreeSet<CSVRecordGroup> recordGroupsForIssueType = recordGroupsByIssueType.get(issueType);
			
			int countGroupRecords = 0;
			
			for (CSVRecordGroup recordGroup : recordGroupsForIssueType)
			{
				countGroupRecords  = recordGroup.records().size();
				countAllRecords   += countGroupRecords;
//				log.debug("{} records for group with key {}", countGroupRecords, recordGroup.key());
			}

			log.debug(
					"{} record groups for issue type {}",
					recordGroupsForIssueType.size(),
					issueType.typeName());
		}
		
		log.debug("processed  {} records", countAllRecords);
	}

	public static ReportIssueTypeGroupsCount create(
			@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		return new ReportIssueTypeGroupsCount(recordGroupsByIssueType);
	}
}