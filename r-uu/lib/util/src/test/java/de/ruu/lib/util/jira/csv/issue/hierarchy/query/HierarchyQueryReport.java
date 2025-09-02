package de.ruu.lib.util.jira.csv.issue.hierarchy.query;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Set;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;

import de.ruu.lib.util.jira.csv.issue.IssueTypes.Bug;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Epic;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.FeatureRequest;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Initiative;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Story;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.SubTaskEpic;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.SubTaskStory;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Task;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.TechnicalTask;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

public interface HierarchyQueryReport
{
	@NonNull Path path();

	@NonNull public static Path defaultDirectory()
	{
		return
				Path.of(
						ConfigProvider.getConfig().getValue("path.directory.data.jira.report", String.class));
	}
	
	void report(@NonNull Set<Initiative> initiatives) throws IOException;

	@RequiredArgsConstructor
	@Getter @Accessors(fluent = true)
	class HierarchyQueryReportSimple implements HierarchyQueryReport
	{
		private final static Config CONFIG = ConfigProvider.getConfig();

		private final static String HEADER_DATA = CONFIG.getValue("header.data.csv", String.class);
		
		@NonNull
		private final Path path;

		@Override public void report(@NonNull Set<Initiative> initiatives) throws IOException
		{
			try (Writer writer = new BufferedWriter(new FileWriter(path.toString(), false)))
			{
				writer.write(HEADER_DATA + "\n");
				for (Initiative initiative : initiatives)
				{
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
						for (FeatureRequest featureRequest : epic.featureRequests())
						{
							writer.write(featureRequest.lastRecord().asCSVRow() + "\n");
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
						for (SubTaskEpic subTaskEpic : epic.subTasks())
						{
							writer.write(subTaskEpic.lastRecord().asCSVRow() + "\n");
						}
					}
				}
			}
		}
	}
	
	static HierarchyQueryReport create(@NonNull Path path) { return new HierarchyQueryReportSimple(path); }
}