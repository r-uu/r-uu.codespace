package de.ruu.lib.util.jira.csv.report;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import de.ruu.lib.util.jira.csv.issue.IssueTypes.Bug;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Epic;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.FeatureRequest;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Initiative;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Story;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.SubTaskStory;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Task;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.TechnicalTask;
import de.ruu.lib.util.jira.csv.issue.hierarchy.Hierarchy;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportHierarchyTreeInfo 
{
	private final static Config CONFIG = ConfigProvider.getConfig();

	private final static Path PATH_FILE_REPORT_HIERARCHY_TREE           =
			Paths.get(CONFIG.getValue("path.file.report.hierarchy.tree"          , String.class));
	private final static Path PATH_FILE_REPORT_HIERARCHY_TREE_FILTERED  =
			Paths.get(CONFIG.getValue("path.file.report.hierarchy.tree.filtered" , String.class));

	private final static String HEADER =
			  CONFIG.getValue("header.data.csv", String.class)
			+ ";T_Initiative"
			+ ";T_Epic"
			+ ";T_Story"
			+ ";T_SubTask"
			+ ";T_Task"
			+ ";T_TechnicalTask"
			+ ";T_Bug"
			+ ";T_FeatureRequest";

	@NonNull private final Hierarchy hierarchy;

	private ReportHierarchyTreeInfo(@NonNull Hierarchy hierarchy)
	{
		this.hierarchy = hierarchy;
	}

	public void report() throws IOException
	{
		try (Writer writer = new BufferedWriter(new FileWriter(PATH_FILE_REPORT_HIERARCHY_TREE.toString(), false)))
		{
			writer.write(HEADER + "\n");

			for (String key : hierarchy.initiativesByKey().keySet())
			{
				Initiative initiative = hierarchy.initiativesByKey().get(key);

				writer.write(initiative.lastRecord().asCSVRow() + treeInfo(initiative) + "\n");
				
				for (Epic epic : initiative.epics())
				{
					writer.write(epic.lastRecord().asCSVRow() + treeInfo(epic) + "\n");

					for (Story story : epic.stories())
					{
						writer.write(story.lastRecord().asCSVRow() + treeInfo(story) + "\n");
						
						for (SubTaskStory subTask : story.subTasks())
						{
							writer.write(subTask.lastRecord().asCSVRow() + treeInfo(subTask) + "\n");
						}
					}
					for (Task task : epic.tasks())
					{
						writer.write(task          .lastRecord().asCSVRow() + treeInfo(task          ) + "\n");
					}
					for (TechnicalTask technicalTask : epic.technicalTasks())
					{
						writer.write(technicalTask .lastRecord().asCSVRow() + treeInfo(technicalTask ) + "\n");
					}
					for (Bug bug : epic.bugs())
					{
						writer.write(bug           .lastRecord().asCSVRow() + treeInfo(bug           ) + "\n");
					}
					for (FeatureRequest featureRequest : epic.featureRequests())
					{
						writer.write(featureRequest.lastRecord().asCSVRow() + treeInfo(featureRequest) + "\n");
					}
				}
			}
		}
	}

	public void reportFiltered() throws IOException
	{
		try (Writer writer = new BufferedWriter(new FileWriter(PATH_FILE_REPORT_HIERARCHY_TREE_FILTERED.toString(), false)))
		{
			writer.write(HEADER + "\n");

			for (String key : hierarchy.initiativesByKey().keySet())
			{
				Initiative initiative = hierarchy.initiativesByKey().get(key);
				
				if (accept(initiative))
				{
					writer.write(initiative.lastRecord().asCSVRow() + treeInfo(initiative) + "\n");
					
					for (Epic epic : initiative.epics())
					{
						if (accept(epic))
						{
							writer.write(epic.lastRecord().asCSVRow() + treeInfo(epic) + "\n");

							for (Story story : epic.stories())
							{
								if (accept(story))
								{
									writer.write(story.lastRecord().asCSVRow() + treeInfo(story) + "\n");
									
									for (SubTaskStory subTask : story.subTasks())
									{
										if (accept(subTask))
										{
											writer.write(subTask.lastRecord().asCSVRow() + treeInfo(subTask) + "\n");
										}
									}
								}
							}
							for (Task task : epic.tasks())
							{
								if (accept(task))
								{
									writer.write(task          .lastRecord().asCSVRow() + treeInfo(task          ) + "\n");
								}
							}
							for (TechnicalTask technicalTask : epic.technicalTasks())
							{
								if (accept(technicalTask))
								{
									writer.write(technicalTask .lastRecord().asCSVRow() + treeInfo(technicalTask ) + "\n");
								}
							}
							for (Bug bug : epic.bugs())
							{
								if (accept(bug))
								{
									writer.write(bug           .lastRecord().asCSVRow() + treeInfo(bug           ) + "\n");
								}
							}
							for (FeatureRequest featureRequest : epic.featureRequests())
							{
								if (accept(featureRequest))
								{
									writer.write(featureRequest.lastRecord().asCSVRow() + treeInfo(featureRequest) + "\n");
								}
							}
						}
					}
				}
			}
		}
		log.debug("rejected initiatives     {}", rejectedInitiatives    );
		log.debug("rejected epics           {}", rejectedEpics          );
		log.debug("rejected stories         {}", rejectedStories        );
		log.debug("rejected subTasks        {}", rejectedSubTasks       );
		log.debug("rejected tasks           {}", rejectedTasks          );
		log.debug("rejected technicalTasks  {}", rejectedTechnicalTasks );
		log.debug("rejected bugs            {}", rejectedBugs           );
		log.debug("rejected featureRequests {}", rejectedFeatureRequests);
	}

	private String treeInfo(Initiative initiative)
	{
		return
				  ";" + initiative.lastRecord().key()
				+ ";" // no epic
				+ ";" // no story
				+ ";" // no subTask
				+ ";" // no task
				+ ";" // no technicalTask
				+ ";" // no bug
				+ ";" // no featureRequest
				;
	}

	private String treeInfo(Epic epic)
	{
		return
				  ";" + epic.initiative().lastRecord().key()
				+ ";" + epic.lastRecord().key()
				+ ";" // no story
				+ ";" // no subTask
				+ ";" // no task
				+ ";" // no technicalTask
				+ ";" // no bug
				+ ";" // no featureRequest
				;
	}

	private String treeInfo(Story story)
	{
		return
				  ";" + story.epic().initiative().lastRecord().key()
				+ ";" + story.epic().lastRecord().key()
				+ ";" + story.lastRecord().key()
				+ ";" // no subTask
				+ ";" // no task
				+ ";" // no technicalTask
				+ ";" // no bug
				+ ";" // no featureRequest
				;
	}

	private String treeInfo(SubTaskStory subTask)
	{
		return
				  ";" + subTask.story().epic().initiative().lastRecord().key()
				+ ";" + subTask.story().epic().lastRecord().key()
				+ ";" + subTask.story().lastRecord().key()
				+ ";" + subTask.lastRecord().key()
				+ ";" // no task
				+ ";" // no technicalTask
				+ ";" // no bug
				+ ";" // no featureRequest
				;
	}

	private String treeInfo(Task task)
	{
		return
				  ";" + task.epic().initiative().lastRecord().key()
				+ ";" + task.epic().lastRecord().key()
				+ ";" // no story
				+ ";" // no subTask
				+ ";" + task.lastRecord().key()
				+ ";" // no technicalTask
				+ ";" // no bug
				+ ";" // no featureRequest
				;
	}

	private String treeInfo(TechnicalTask technicalTask)
	{
		return
				  ";" + technicalTask.epic().initiative().lastRecord().key()
				+ ";" + technicalTask.epic().lastRecord().key()
				+ ";" // no story
				+ ";" // no subTask
				+ ";" // no task
				+ ";" + technicalTask.lastRecord().key()
				+ ";" // no bug
				+ ";" // no featureRequest
				;
	}

	private String treeInfo(Bug bug)
	{
		return
				  ";" + bug.epic().initiative().lastRecord().key()
				+ ";" + bug.epic().lastRecord().key()
				+ ";" // no story
				+ ";" // no subTask
				+ ";" // no task
				+ ";" // no technicalTask
				+ ";" + bug.lastRecord().key()
				+ ";" // no featureRequest
				;
	}

	private String treeInfo(FeatureRequest featureRequest)
	{
		return
				  ";" + featureRequest.epic().initiative().lastRecord().key()
				+ ";" + featureRequest.epic().lastRecord().key()
				+ ";" // no story
				+ ";" // no subTask
				+ ";" // no task
				+ ";" // no technicalTask
				+ ";" // no bug
				+ ";" + featureRequest.lastRecord().key()
				;
	}

	private int rejectedInitiatives = 0;
	private boolean accept(Initiative initiative)
	{
		boolean result =
//			   initiative.record().status().equals("Fertig")
//			|| initiative.record().status().equals("Verworfen")
//			   initiative.record().status().equals("SYNTHETIC") == false
				true
		;
		if (result == false) rejectedInitiatives++;
		return result;
	}

	private int rejectedEpics = 0;
	private boolean accept(Epic epic)
	{
		boolean result =
//				   epic.record().status().equals("Fertig")
//				|| epic.record().status().equals("Verworfen")
//				|| epic.record().status().equals("on hold")
//				   epic.record().fixVersion().contains("2.2.12")
//				   epic.record().status().equals("SYNTHETIC")
				true
		;
		if (result == false) rejectedEpics++;
		return result;
	}

	private int rejectedStories = 0;
	private boolean accept(Story story)
	{
		boolean result =
//				   story.record().status().equals("Fertig")
//				|| story.record().status().equals("Verworfen")
//				   true
				   story.epic().lastRecord().key().contains("55392")
				|| story.epic().lastRecord().key().contains("55724")
				|| story.epic().lastRecord().key().contains("54884")
				|| story.epic().lastRecord().key().contains("56895")
				|| story.epic().lastRecord().key().contains("56797")
				|| story.epic().lastRecord().key().contains("56467")
				|| story.epic().lastRecord().key().contains("57132")
				|| story.epic().lastRecord().key().contains("57459")
				;
		if (result == false) rejectedStories++;
		return result;
	}

	private int rejectedSubTasks = 0;
	private boolean accept(SubTaskStory subTask)
	{
		boolean result =
				false
				;
		if (result == false) rejectedSubTasks++;
		return result;
	}

	private int rejectedBugs = 0;
	private boolean accept(Bug bug)
	{
		boolean result =
				false
				;
		if (result == false) rejectedBugs++;
		return result;
	}

	private int rejectedFeatureRequests = 0;
	private boolean accept(FeatureRequest featureRequest)
	{
		boolean result =
				false
				;
		if (result == false) rejectedFeatureRequests++;
		return result;
	}

	private int rejectedTechnicalTasks = 0;
	private boolean accept(TechnicalTask technicalTask)
	{
		boolean result =
				false
				;
		if (result == false) rejectedTechnicalTasks++;
		return result;
	}

	private int rejectedTasks = 0;
	private boolean accept(Task task)
	{
		boolean result =
				false
				;
		if (result == false) rejectedTasks++;
		return result;
	}

	public static ReportHierarchyTreeInfo create(@NonNull Hierarchy hierarchy) { return new ReportHierarchyTreeInfo(hierarchy); }
}