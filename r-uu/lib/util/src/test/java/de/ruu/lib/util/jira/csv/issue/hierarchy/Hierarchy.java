package de.ruu.lib.util.jira.csv.issue.hierarchy;

import static de.ruu.lib.util.jira.csv.issue.IssueType.Bug;
import static de.ruu.lib.util.jira.csv.issue.IssueType.Epic;
import static de.ruu.lib.util.jira.csv.issue.IssueType.FeatureRequest;
import static de.ruu.lib.util.jira.csv.issue.IssueType.Initiative;
import static de.ruu.lib.util.jira.csv.issue.IssueType.Story;
import static de.ruu.lib.util.jira.csv.issue.IssueType.SubTask;
import static de.ruu.lib.util.jira.csv.issue.IssueType.Task;
import static de.ruu.lib.util.jira.csv.issue.IssueType.TechnicalTask;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import de.ruu.lib.util.jira.csv.issue.IssueType;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Bug;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Epic;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.FeatureRequest;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Initiative;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Story;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.SubTaskEpic;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.SubTaskStory;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Task;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.TechnicalTask;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroup;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Accessors(fluent = true)
public class Hierarchy
{
	private final @NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType;
	private final          Map<String   ,         CSVRecordGroup>  recordGroupsByKey = new TreeMap<>();

	private final Map<String, Initiative>     initiativesByKey     = new TreeMap<>();
	private final Map<String, Epic>           epicsByKey           = new TreeMap<>();
	private final Map<String, Story>          storiesByKey         = new TreeMap<>();
	private final Map<String, SubTaskStory>   subTasksStoryByKey   = new TreeMap<>();
	private final Map<String, SubTaskEpic>    subTasksEpicByKey    = new TreeMap<>();
	private final Map<String, Task>           tasksByKey           = new TreeMap<>();
	private final Map<String, TechnicalTask>  technicalTasksByKey  = new TreeMap<>();
	private final Map<String, Bug>            bugsByKey            = new TreeMap<>();
	private final Map<String, FeatureRequest> featureRequestsByKey = new TreeMap<>();
	
	private Hierarchy(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		this.recordGroupsByIssueType = recordGroupsByIssueType;

		for (TreeSet<CSVRecordGroup> groups : recordGroupsByIssueType.values())
		{
			for (CSVRecordGroup group : groups)
			{
				CSVRecordGroup previous = recordGroupsByKey.put(group.key(), group);
				if ((previous != null) && (previous != group)) log.warn("replaced previous group in record group by keys");
			}
		}

		initInitiatives    (recordGroupsByIssueType);
		initEpics          (recordGroupsByIssueType);
		initStories        (recordGroupsByIssueType);
		initSubTasks       (recordGroupsByIssueType);
		initTasks          (recordGroupsByIssueType);
		initTechnicalTasks (recordGroupsByIssueType);
		initBugs           (recordGroupsByIssueType);
		initFeatureRequests(recordGroupsByIssueType);

//		log.debug("records in hierarchy  {}", countRecordsInHierarchy ());
//		log.debug("records in categories {}", countRecordsInCategories());
		
//		log.debug("initiatives      overall                 {}", initiativesByKey    .size());
//		log.debug("epics            overall                 {}", epicsByKey          .size());
//		log.debug("stories          overall                 {}", storiesByKey        .size());
//		log.debug("subTs            overall                 {}", subTasksByKey       .size());
//		log.debug("tasks            overall                 {}", tasksByKey          .size());
//		log.debug("technical tasks  overall                 {}", technicalTasksByKey .size());
//		log.debug("bugs             overall                 {}", bugsByKey           .size());
//		log.debug("feature requests overall                 {}", featureRequestsByKey.size());
//		log.debug("epics            in synthetic initiative {}", IssueTypes.Initiative.syntheticInstance().epics          ().size());
//		log.debug("stories          in synthetic epic       {}", IssueTypes.Epic      .syntheticInstance().stories        ().size());
//		log.debug("subTasks         in synthetic story      {}", IssueTypes.Story     .syntheticInstance().subTasks       ().size());
//		log.debug("tasks            in synthetic epic       {}", IssueTypes.Epic      .syntheticInstance().tasks          ().size());
//		log.debug("technical tasks  in synthetic epic       {}", IssueTypes.Epic      .syntheticInstance().technicalTasks ().size());
//		log.debug("bugs             in synthetic epic       {}", IssueTypes.Epic      .syntheticInstance().bugs           ().size());
//		log.debug("feature requests in synthetic epic       {}", IssueTypes.Epic      .syntheticInstance().featureRequests().size());
	}

	private void initInitiatives(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		for (CSVRecordGroup recordGroup : recordGroupsByIssueType.get(Initiative))
		{
			initiativesByKey.put(recordGroup.key(), new Initiative(recordGroup));
		}
	}

	private void initEpics(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		for (CSVRecordGroup recordGroup : recordGroupsByIssueType.get(Epic))
		{
			Initiative initiative = initiativesByKey.get(recordGroup.records().last().parentId());

			if (initiative == null)
			{
				CSVRecordGroup.addSyntheticEpics(recordGroup);
			}
			else
			{
				Epic epic = new Epic(recordGroup, initiative);

				initiative.epics().add(epic);
				epicsByKey.put(recordGroup.key(), epic);
			}

		}
	}

	private void initStories(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		for (CSVRecordGroup recordGroup : recordGroupsByIssueType.get(Story))
		{
			Epic epic = epicsByKey.get(recordGroup.records().last().epicLink());

			if (epic == null)
			{
				CSVRecordGroup.addSyntheticStories(recordGroup);
			}
			else
			{
				Story story = new Story(recordGroup, epic);

				epic.stories().add(story);
				storiesByKey.put(recordGroup.key(), story);
			}
		}
	}

	private void initSubTasks(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		for (CSVRecordGroup recordGroup : recordGroupsByIssueType.get(SubTask))
		{
			String parentKey = recordGroup.records().last().parentKey();

			Story story = storiesByKey.get(parentKey);

			if (story == null)
			{
				Epic epic = epicsByKey.get(parentKey);

				if (epic == null)
				{
					CSVRecordGroup.addSyntheticEpics(recordGroup);
				}
				else
				{
					SubTaskEpic subTask = new SubTaskEpic(recordGroup, epic);

					epic.subTasks().add(subTask);
					subTasksEpicByKey.put(recordGroup.key(), subTask);
				}
			}
			else
			{
				SubTaskStory subTask = new SubTaskStory(recordGroup, story);

				story.subTasks().add(subTask);
				subTasksStoryByKey.put(recordGroup.key(), subTask);
			}
		}
	}

	private void initTasks(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		for (CSVRecordGroup recordGroup : recordGroupsByIssueType.get(Task))
		{
			Epic epic = epicsByKey.get(recordGroup.records().last().epicLink());

			if (epic == null)
			{
				CSVRecordGroup.addSyntheticEpics(recordGroup);
			}
			else
			{
				Task task = new Task(recordGroup, epic);

				epic.tasks().add(task);
				tasksByKey.put(recordGroup.key(), task);
			}
		}
	}

	private void initTechnicalTasks(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		for (CSVRecordGroup recordGroup : recordGroupsByIssueType.get(TechnicalTask))
		{
			Epic epic = epicsByKey.get(recordGroup.records().last().epicLink());

			if (epic == null)
			{
				CSVRecordGroup.addSyntheticEpics(recordGroup);
			}
			else
			{
				TechnicalTask technicalTask = new TechnicalTask(recordGroup, epic);
				
				epic.technicalTasks().add(technicalTask);
				technicalTasksByKey.put(recordGroup.key(), technicalTask);
			}
		}
	}

	private void initBugs(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		for (CSVRecordGroup recordGroup : recordGroupsByIssueType.get(Bug))
		{
			Epic epic = epicsByKey.get(recordGroup.records().last().epicLink());

			if (epic == null)
			{
				CSVRecordGroup.addSyntheticEpics(recordGroup);
			}
			else
			{
				Bug bug = new Bug(recordGroup, epic);
				
				epic.bugs().add(bug);
				bugsByKey.put(recordGroup.key(), bug);
			}
		}
	}

	private void initFeatureRequests(@NonNull Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		for (CSVRecordGroup recordGroup : recordGroupsByIssueType.get(FeatureRequest))
		{
			Epic epic = epicsByKey.get(recordGroup.records().last().epicLink());

			if (epic == null)
			{
				CSVRecordGroup.addSyntheticEpics(recordGroup);
			}
			else
			{
				FeatureRequest featureRequest = new FeatureRequest(recordGroup, epic);

				epic.featureRequests().add(featureRequest);
				featureRequestsByKey.put(recordGroup.key(), featureRequest);
			}
		}
	}

	public static Hierarchy create(Map<IssueType, TreeSet<CSVRecordGroup>> recordGroupsByIssueType)
	{
		return new Hierarchy(recordGroupsByIssueType);
	}
}