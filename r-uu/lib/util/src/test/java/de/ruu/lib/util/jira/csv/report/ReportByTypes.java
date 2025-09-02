package de.ruu.lib.util.jira.csv.report;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import de.ruu.lib.util.jira.csv.issue.IssueType;
import de.ruu.lib.util.jira.csv.raw.CSVRecord;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroup;
import lombok.NonNull;

public class ReportByTypes 
{
	private final static Config CONFIG = ConfigProvider.getConfig();

	private final static Path PATH_FILE_REPORT_BY_TYPES  = Paths.get(CONFIG.getValue("path.file.report.by.types" , String.class));

	private final static String HEADER = CONFIG.getValue("header.csv", String.class);
	
	@NonNull private final Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType;

	private ReportByTypes(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		this.recordGroupsByIssueType = recordGroupsByIssueType;
	}

	public void report() throws IOException
	{
//		log.debug("found {} issue types", recordGroupsByIssueType.size());

		try (Writer writer = new BufferedWriter(new FileWriter(PATH_FILE_REPORT_BY_TYPES.toString(), false)))
		{
			writer.write(HEADER + "\n");

			for (IssueType issueType : recordGroupsByIssueType.keySet())
			{
				Set<CSVRecordGroup> recordGroups = recordGroupsByIssueType.get(issueType);
				
//				log.debug("found {} record groups for issue type {}", recordGroups.size(), issueType);

				for (CSVRecordGroup group : recordGroups)
				{
//					log.debug("found {} records for record group {}", group.records().size(), group.key());

					for (CSVRecord record : group.records())
					{
						writer.write(record.asCSVRow() + "\n");
					}
				}
			}
		}
	}
	
	public static ReportByTypes create(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		return new ReportByTypes(recordGroupsByIssueType);
	}
}