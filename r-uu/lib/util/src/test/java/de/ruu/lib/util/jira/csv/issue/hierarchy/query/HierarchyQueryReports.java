package de.ruu.lib.util.jira.csv.issue.hierarchy.query;

import java.nio.file.Path;
import java.nio.file.Paths;

import de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryReport.HierarchyQueryReportSimple;
import lombok.NonNull;

public interface HierarchyQueryReports
{
	class StoriesWithEffortConsumed extends HierarchyQueryReportSimple
	{
		public StoriesWithEffortConsumed(@NonNull Path path) { super(path); }
	}
	public static HierarchyQueryReport storiesWithEffortConsumed()
	{
		return
				new StoriesWithEffortConsumed(
						Paths.get(
								HierarchyQueryReport.defaultDirectory().toString(),
								StoriesWithEffortConsumed.class.getSimpleName() + ".csv"));
	}
	class InitiativesInReportPlanned extends HierarchyQueryReportSimple
	{
		public InitiativesInReportPlanned(@NonNull Path path) { super(path); }
	}
	public static HierarchyQueryReport initiativesInReportPlanned()
	{
		return
				new InitiativesInReportPlanned(
						Paths.get(
								HierarchyQueryReport.defaultDirectory().toString(),
								InitiativesInReportPlanned.class.getSimpleName() + ".csv"));
	}
}