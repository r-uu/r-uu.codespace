package de.ruu.lib.util.jira.csv.raw;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import de.ruu.lib.util.jira.csv.issue.IssueType;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVRecordGroupsFactory
{
	public static TreeMap<String, CSVRecordGroup> recordGroupsByKey(
			@NonNull Map<String, TreeSet<CSVRecord>> recordsByKey)
	{
		TreeMap<String, CSVRecordGroup> result = new TreeMap<>();
		for (String key : recordsByKey.keySet())
		{
			result.put(key, new CSVRecordGroup(recordsByKey.get(key)));
		}
		return result;
	}

	public static TreeMap<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType(
			@NonNull Map<String, CSVRecordGroup> recordGroupsByKey)
	{
//		log.debug("processing {} record groups", recordGroupsByKey.size());

		TreeMap<IssueType, TreeSet<CSVRecordGroup>> result = new TreeMap<>(IssueType.compareByTypeName());
		
		// create entry in result for each issue type
		for (IssueType issueType : IssueType.values())
		{
			result.put(issueType, new TreeSet<CSVRecordGroup>(CSVRecordGroup.comparator()));
		}
		
		for (String key : recordGroupsByKey.keySet())
		{
//			log.debug("processing record group for key {}", key);

			CSVRecordGroup currentRecordGroup = recordGroupsByKey.get(key);
			IssueType      currentIssueType   = currentRecordGroup.type();

//			log.debug(
//					"current record group for key {} contains {} records, current record group issue type is {}",
//					key,
//					currentRecordGroup.records().size(),
//					currentIssueType);

			// try to find record groups for type in result
			TreeSet<CSVRecordGroup> recordGroupsContainerForCurrentIssueType = result.get(currentIssueType);

//			if (recordGroupsContainerForCurrentIssueType == null)
//			{
////				log.debug("no record groups container found for issue type {} in result", currentIssueType);
//
//				// no record group for type in result so far, add one now:
//				recordGroupsContainerForCurrentIssueType = new TreeSet<>(CSVRecordGroup.comparator());
//				result.put(currentIssueType, recordGroupsContainerForCurrentIssueType);
////				log.debug(
////						"created new record groups container for issue type {}, result now contains {} record groups container",
////						currentIssueType,
////						result.size());
//			}
//			else
//			{
////				log.debug("record groups container found in result for type {}", currentIssueType);
//			}

			// now that we have a (new) record groups container for type, add record group
			if (recordGroupsContainerForCurrentIssueType.add(currentRecordGroup) == false)
			{
				log.warn("could not add record group to record groups container for key " + key + " to result");
			}
			else
			{
//				log.debug("added record group to record groups container of type {}, record groups container size {}",
//						currentIssueType,
//						recordGroupsContainerForCurrentIssueType.size());
			}
		}

//		log.debug("processed  {} records", countRecords(result));

		return result;
	}

	public static int countRecords(TreeMap<IssueType, TreeSet<CSVRecordGroup>> recordsByType)
	{
		int result = 0;
		for (IssueType issueType : recordsByType.keySet())
		{
			TreeSet<CSVRecordGroup> recordGroups = recordsByType.get(issueType);
			
			for (CSVRecordGroup recordGroup : recordGroups)
			{
				result += recordGroup.records().size();
			}
		}
		return result;
	}
}