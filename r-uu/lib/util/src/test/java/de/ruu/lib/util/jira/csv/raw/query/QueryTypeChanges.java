package de.ruu.lib.util.jira.csv.raw.query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.ruu.lib.util.jira.csv.issue.IssueType;
import de.ruu.lib.util.jira.csv.raw.CSVRecord;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroup;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

public class QueryTypeChanges
{
	@NonNull private final Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType;

	public QueryTypeChanges(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		this.recordGroupsByIssueType = recordGroupsByIssueType;
	}

	public List<TypeChange> query()
	{
		List<TypeChange> result = new ArrayList<QueryTypeChanges.TypeChange>();
		
		for (IssueType key : recordGroupsByIssueType.keySet())
		{
			Set<CSVRecordGroup> recordGroups = recordGroupsByIssueType.get(key);

			for (CSVRecordGroup recordGroup : recordGroups)
			{
				IssueType formerType = null;

				for (CSVRecord record : recordGroup.records())
				{
					if (formerType == null) formerType = record.type();

					if (record.type().equals(formerType) == false)
					{
						result.add(new TypeChange(recordGroup.key(), record.date(), formerType, record.type()));
						formerType = record.type();
					}
				}
			}
		}

		return result;
	}

	@RequiredArgsConstructor @Getter @Accessors(fluent = true)
	public class TypeChange
	{
		@NonNull private final String    key;
		@NonNull private final LocalDate date;
		@NonNull private final IssueType from;
		@NonNull private final IssueType to;
	}

	public static QueryTypeChanges create(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		return new QueryTypeChanges(recordGroupsByIssueType);
	}
}