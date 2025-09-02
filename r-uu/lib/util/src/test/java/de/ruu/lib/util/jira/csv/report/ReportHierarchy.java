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

public class ReportHierarchy 
{
	private final static Config CONFIG = ConfigProvider.getConfig();

	private final static Path PATH_FILE_REPORT_HIERARCHY           = Paths.get(CONFIG.getValue("path.file.report.hierarchy" , String.class));
	private final static Path PATH_FILE_REPORT_HIERARCHY_FILTERED  = Paths.get(CONFIG.getValue("path.file.report.hierarchy.filtered" , String.class));

	private final static String HEADER = CONFIG.getValue("header.csv", String.class);
	
	@NonNull private final Hierarchy hierarchy;

	private ReportHierarchy(@NonNull Hierarchy hierarchy)
	{
		this.hierarchy = hierarchy;
	}

	public void report() throws IOException
	{
		try (Writer writer = new BufferedWriter(new FileWriter(PATH_FILE_REPORT_HIERARCHY.toString(), false)))
		{
			writer.write(HEADER + "\n");

			for (String key : hierarchy.initiativesByKey().keySet())
			{
				Initiative initiative = hierarchy.initiativesByKey().get(key);

				writer.write(initiative.lastRecord().asCSVRow() + "\n");
				
				for (Epic epic : initiative.epics())
				{
					writer.write(epic.lastRecord().asCSVRow() + "\n");
					
					for (Story story : epic.stories())
					{
						writer.write(story.lastRecord().asCSVRow() + "\n");
						
						for (SubTaskStory subTask : story.subTasks())
						{
							writer.write(subTask.lastRecord().asCSVRow() + "\n");
						}
					}
					for (Task task : epic.tasks())
					{
						writer.write(task.lastRecord().asCSVRow() + "\n");
					}
					for (TechnicalTask technicalTask : epic.technicalTasks())
					{
						writer.write(technicalTask.lastRecord().asCSVRow() + "\n");
					}
					for (Bug bug : epic.bugs())
					{
						writer.write(bug.lastRecord().asCSVRow() + "\n");
					}
					for (FeatureRequest featureRequest : epic.featureRequests())
					{
						writer.write(featureRequest.lastRecord().asCSVRow() + "\n");
					}
				}
			}
		}
	}

	public void reportFiltered() throws IOException
	{
		try (Writer writer = new BufferedWriter(new FileWriter(PATH_FILE_REPORT_HIERARCHY_FILTERED.toString(), false)))
		{
			writer.write(HEADER + "\n");

			for (String key : hierarchy.initiativesByKey().keySet())
			{
				Initiative initiative = hierarchy.initiativesByKey().get(key);
				
				if (reject(initiative) == false)
				{
					writer.write(initiative.lastRecord().asCSVRow() + "\n");
					
					for (Epic epic : initiative.epics())
					{
						if (reject(epic) == false)
						{
							writer.write(epic.lastRecord().asCSVRow() + "\n");
							
							for (Story story : epic.stories())
							{
								if (reject(story) == false)
								{
									writer.write(story.lastRecord().asCSVRow() + "\n");
									
									for (SubTaskStory subTask : story.subTasks())
									{
										writer.write(subTask.lastRecord().asCSVRow() + "\n");
									}
								}
							}
							for (Task task : epic.tasks())
							{
								writer.write(task.lastRecord().asCSVRow() + "\n");
							}
							for (TechnicalTask technicalTask : epic.technicalTasks())
							{
								writer.write(technicalTask.lastRecord().asCSVRow() + "\n");
							}
							for (Bug bug : epic.bugs())
							{
								writer.write(bug.lastRecord().asCSVRow() + "\n");
							}
							for (FeatureRequest featureRequest : epic.featureRequests())
							{
								writer.write(featureRequest.lastRecord().asCSVRow() + "\n");
							}
						}
					}
				}
			}
		}
	}

	private boolean reject(Initiative initiative)
	{
		return
			   initiative.lastRecord().status().equals("Fertig")
			|| initiative.lastRecord().status().equals("Verworfen")
			|| initiative.lastRecord().status().equals("SYNTHETIC")
		;
	}

	private boolean reject(Epic epic)
	{
		return
				   epic.lastRecord().status().equals("Fertig")
				|| epic.lastRecord().status().equals("Verworfen")
				|| epic.lastRecord().status().equals("on hold")
				|| epic.lastRecord().status().equals("SYNTHETIC")
		;
	}

	private boolean reject(Story story)
	{
		return
				   story.lastRecord().status().equals("Fertig")
				|| story.lastRecord().status().equals("Verworfen")
				;
	}

	public static ReportHierarchy create(@NonNull Hierarchy hierarchy) { return new ReportHierarchy(hierarchy); }
}