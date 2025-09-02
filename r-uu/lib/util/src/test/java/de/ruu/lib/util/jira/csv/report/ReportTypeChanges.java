package de.ruu.lib.util.jira.csv.report;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import de.ruu.lib.util.jira.csv.issue.IssueType;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroup;
import de.ruu.lib.util.jira.csv.raw.query.QueryTypeChanges;
import de.ruu.lib.util.jira.csv.raw.query.QueryTypeChanges.TypeChange;
import lombok.NonNull;

//@Slf4j
public class ReportTypeChanges 
{
	private final static Config CONFIG = ConfigProvider.getConfig();

	private final static Path PATH_FILE_REPORT_TYPE_CHANGES  = Paths.get(CONFIG.getValue("path.file.report.type.changes" , String.class));

	private final static String HEADER = "type old;type new;key;date";
	
	@NonNull private final Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType;

	private ReportTypeChanges(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		this.recordGroupsByIssueType = recordGroupsByIssueType;
	}

	public void report() throws IOException
	{
		QueryTypeChanges query = QueryTypeChanges.create(recordGroupsByIssueType);

		try (Writer writer = new BufferedWriter(new FileWriter(PATH_FILE_REPORT_TYPE_CHANGES.toString(), false)))
		{
			writer.write(HEADER + "\n");

			for (TypeChange typeChange : query.query())
			{
//				String message =
//						"type change from " + typeChange.from() + " to " + typeChange.to() + " for jira issue with key " + typeChange.key()+ " on " + typeChange.date();
//						STR.
//"""type change from \{ typeChange.from() } to \{ typeChange.to() } for jira issue with key \{ typeChange.key() } on \{ typeChange.date() }"""
						;
//				log.debug(message);
				writer.write(typeChange.from() + ";" + typeChange.to() + ";" + typeChange.key() + ";" + typeChange.date() + "\n");
			}
		}
	}
	
	public static ReportTypeChanges create(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		return new ReportTypeChanges(recordGroupsByIssueType);
	}
}