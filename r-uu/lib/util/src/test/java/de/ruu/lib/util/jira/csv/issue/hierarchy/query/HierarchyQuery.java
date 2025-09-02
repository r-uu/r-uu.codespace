package de.ruu.lib.util.jira.csv.issue.hierarchy.query;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import de.ruu.lib.util.jira.csv.issue.IssueTypes.Bug;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Epic;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.FeatureRequest;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Initiative;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Story;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.SubTaskEpic;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.SubTaskStory;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.Task;
import de.ruu.lib.util.jira.csv.issue.IssueTypes.TechnicalTask;
import de.ruu.lib.util.jira.csv.issue.hierarchy.Hierarchy;
import lombok.NonNull;

public interface HierarchyQuery
{
	@NonNull Set<Initiative> query(@NonNull Hierarchy hierarchy, @NonNull HierarchyQueryConfig config);
	
	class HierarchyQuerySimple implements HierarchyQuery
	{
		@Override
		public @NonNull Set<Initiative> query(
				@NonNull Hierarchy hierarchy, @NonNull HierarchyQueryConfig config)
		{
			Set<Initiative> result = new TreeSet<>();

			Map<String, Initiative    > newInitiativesByKey     = new TreeMap<>();
			Map<String, Epic          > newEpicsByKey           = new TreeMap<>();
			Map<String, Story         > newStoriesByKey         = new TreeMap<>();
			Map<String, SubTaskStory  > newSubTasksStoryByKey   = new TreeMap<>();
			Map<String, SubTaskEpic   > newSubTasksEpicByKey    = new TreeMap<>();
			Map<String, TechnicalTask > newTechnicalTasksByKey  = new TreeMap<>();
			Map<String, Task          > newTasksByKey           = new TreeMap<>();
			Map<String, Bug           > newBugsByKey            = new TreeMap<>();
			Map<String, FeatureRequest> newFeatureRequestsByKey = new TreeMap<>();

			for (Initiative initiative : hierarchy.initiativesByKey().values())
			{
				if (config.predicateInitiative().isPresent())
				{
					// add new initiatives for all initiatives that match initiative predicate
					if (config.predicateInitiative().get().test(initiative))
					{
						Initiative newInitiative = new Initiative(initiative.recordGroup());
						newInitiativesByKey.put(newInitiative.recordGroup().lastRecord().key(), newInitiative);
						result.add(newInitiative);
					}
				}

				if (config.predicateEpic().isPresent())
				{
					for (Epic epic : initiative.epics())
					{
						// add initiatives that have epics that match epic predicate
						if (config.predicateEpic().get().test(epic))
						{
							Initiative newInitiative = newInitiativesByKey.get(epic.initiative().lastRecord().key());

							if (newInitiative == null)
							{
								newInitiative = new Initiative(epic.initiative().recordGroup());
								newInitiativesByKey.put(newInitiative.lastRecord().key(), newInitiative);
							}

							Epic newEpic = newEpicsByKey.get(epic.lastRecord().key());
							
							if (newEpic == null)
							{
								newEpic = new Epic(epic.recordGroup(), newInitiative);
								newEpicsByKey.put(newEpic.lastRecord().key(), newEpic);
								newInitiative.epics().add(newEpic);
							}

							result.add(newInitiative);
						}
					}
				}

				if (config.predicateStory().isPresent())
				{
					for (Epic epic : initiative.epics())
					{
						for (Story story : epic.stories())
						{
							// add initiatives that have epics that have stories that match predicate
							if (config.predicateStory().get().test(story))
							{
								Initiative newInitiative = newInitiativesByKey.get(epic.initiative().recordGroup().lastRecord().key());

								if (newInitiative == null)
								{
									newInitiative = new Initiative(epic.initiative().recordGroup());
									newInitiativesByKey.put(newInitiative.lastRecord().key(), newInitiative);
								}

								Epic newEpic = newEpicsByKey.get(epic.lastRecord().key());
								
								if (newEpic == null)
								{
									newEpic = new Epic(epic.recordGroup(), newInitiative);
									newEpicsByKey.put(newEpic.lastRecord().key(), newEpic);
									newInitiative.epics().add(newEpic);
								}

								Story newStory = newStoriesByKey.get(story.lastRecord().key());
								
								if (newStory == null)
								{
									newStory = new Story(story.recordGroup(), newEpic);
									newStoriesByKey.put(newStory.lastRecord().key(), newStory);
									newEpic.stories().add(newStory);
								}
		
								result.add(newInitiative);
							}
						}
					}
				}

				if (config.predicateSubTaskEpic().isPresent())
				{
					for (Epic epic : initiative.epics())
					{
						for (SubTaskEpic subTask : epic.subTasks())
						{
							// add initiatives that have epics that have subtasks that match predicate
							if (config.predicateSubTaskEpic().get().test(subTask))
							{
								Initiative newInitiative = newInitiativesByKey.get(epic.initiative().recordGroup().lastRecord().key());

								if (newInitiative == null)
								{
									newInitiative = new Initiative(epic.initiative().recordGroup());
									newInitiativesByKey.put(newInitiative.lastRecord().key(), newInitiative);
								}

								Epic newEpic = newEpicsByKey.get(epic.lastRecord().key());
								
								if (newEpic == null)
								{
									newEpic = new Epic(epic.recordGroup(), newInitiative);
									newEpicsByKey.put(newEpic.lastRecord().key(), newEpic);
									newInitiative.epics().add(newEpic);
								}

								SubTaskEpic newSubTask = newSubTasksEpicByKey.get(subTask.lastRecord().key());

								if (newSubTask == null)
								{
									newSubTask = new SubTaskEpic(subTask.recordGroup(), newEpic);
									newSubTasksEpicByKey.put(newSubTask.lastRecord().key(), newSubTask);
									newEpic.subTasks().add(newSubTask);
								}

								result.add(newInitiative);
							}
						}
					}
				}

				if (config.predicateSubTaskStory().isPresent())
				{
					for (Epic epic : initiative.epics())
					{
						for (Story story : epic.stories())
						{
							for (SubTaskStory subTask : story.subTasks())
							{
								// add initiatives that have epics that have stories that have subtasks that match predicate
								if (config.predicateSubTaskStory().get().test(subTask))
								{
									Initiative newInitiative = newInitiativesByKey.get(epic.initiative().recordGroup().lastRecord().key());

									if (newInitiative == null)
									{
										newInitiative = new Initiative(epic.initiative().recordGroup());
										newInitiativesByKey.put(newInitiative.lastRecord().key(), newInitiative);
									}

									Epic newEpic = newEpicsByKey.get(epic.lastRecord().key());
									
									if (newEpic == null)
									{
										newEpic = new Epic(epic.recordGroup(), newInitiative);
										newEpicsByKey.put(newEpic.lastRecord().key(), newEpic);
										newInitiative.epics().add(newEpic);
									}

									Story newStory = newStoriesByKey.get(story.lastRecord().key());
									
									if (newStory == null)
									{
										newStory = new Story(story.recordGroup(), newEpic);
										newStoriesByKey.put(newStory.lastRecord().key(), newStory);
										newEpic.stories().add(newStory);
									}
									
									SubTaskStory newSubTask = newSubTasksStoryByKey.get(subTask.lastRecord().key());

									if (newSubTask == null)
									{
										newSubTask = new SubTaskStory(subTask.recordGroup(), newStory);
										newSubTasksStoryByKey.put(newSubTask.lastRecord().key(), newSubTask);
										newStory.subTasks().add(newSubTask);
									}

									result.add(newInitiative);
								}
							}
						}
					}
				}

				if (config.predicateTask().isPresent())
				{
					for (Epic epic : initiative.epics())
					{
						for (Task task : epic.tasks())
						{
							// add initiatives that have epics that have tasks that match predicate
							if (config.predicateTask().get().test(task))
							{
								Initiative newInitiative = newInitiativesByKey.get(epic.initiative().recordGroup().lastRecord().key());

								if (newInitiative == null)
								{
									newInitiative = new Initiative(epic.initiative().recordGroup());
									newInitiativesByKey.put(newInitiative.lastRecord().key(), newInitiative);
								}

								Epic newEpic = newEpicsByKey.get(epic.lastRecord().key());
								
								if (newEpic == null)
								{
									newEpic = new Epic(epic.recordGroup(), newInitiative);
									newEpicsByKey.put(newEpic.lastRecord().key(), newEpic);
									newInitiative.epics().add(newEpic);
								}
								
								Task newTask = newTasksByKey.get(task.lastRecord().key());
								
								if (newTask == null)
								{
									newTask = new Task(task.recordGroup(), newEpic);
									newTasksByKey.put(newTask.lastRecord().key(), newTask);
									newEpic.tasks().add(newTask);
								}

								result.add(newInitiative);
							}
						}
					}
				}

				if (config.predicateTechnicalTask().isPresent())
				{
					for (Epic epic : initiative.epics())
					{
						for (TechnicalTask technicalTask : epic.technicalTasks())
						{
							// add initiatives that have epics that have technical tasks that match predicate
							if (config.predicateTechnicalTask().get().test(technicalTask))
							{
								Initiative newInitiative = newInitiativesByKey.get(epic.initiative().recordGroup().lastRecord().key());

								if (newInitiative == null)
								{
									newInitiative = new Initiative(epic.initiative().recordGroup());
									newInitiativesByKey.put(newInitiative.lastRecord().key(), newInitiative);
								}

								Epic newEpic = newEpicsByKey.get(epic.lastRecord().key());
								
								if (newEpic == null)
								{
									newEpic = new Epic(epic.recordGroup(), newInitiative);
									newEpicsByKey.put(newEpic.lastRecord().key(), newEpic);
									newInitiative.epics().add(newEpic);
								}

								TechnicalTask newTechnicalTask = newTechnicalTasksByKey.get(technicalTask.lastRecord().key());

								if (newTechnicalTask == null)
								{
									newTechnicalTask = new TechnicalTask(technicalTask.recordGroup(), newEpic);
									newTechnicalTasksByKey.put(newTechnicalTask.lastRecord().key(), newTechnicalTask);
									newEpic.technicalTasks().add(newTechnicalTask);
								}

								result.add(newInitiative);
							}
						}
					}
				}

				if (config.predicateBug().isPresent())
				{
					for (Epic epic : initiative.epics())
					{
						for (Bug bug : epic.bugs())
						{
							// add initiatives that have epics that have bugs that match predicate
							if (config.predicateBug().get().test(bug))
							{
								Initiative newInitiative = newInitiativesByKey.get(epic.initiative().recordGroup().lastRecord().key());

								if (newInitiative == null)
								{
									newInitiative = new Initiative(epic.initiative().recordGroup());
									newInitiativesByKey.put(newInitiative.lastRecord().key(), newInitiative);
								}

								Epic newEpic = newEpicsByKey.get(epic.lastRecord().key());
								
								if (newEpic == null)
								{
									newEpic = new Epic(epic.recordGroup(), newInitiative);
									newEpicsByKey.put(newEpic.lastRecord().key(), newEpic);
									newInitiative.epics().add(newEpic);
								}
								
								Bug newBug = newBugsByKey.get(bug.lastRecord().key());
								
								if (newBug == null)
								{
									newBug = new Bug(bug.recordGroup(), newEpic);
									newBugsByKey.put(newBug.lastRecord().key(), newBug);
									newEpic.bugs().add(newBug);
								}

								result.add(newInitiative);
							}
						}
					}
				}

				if (config.predicateFeatureRequest().isPresent())
				{
					for (Epic epic : initiative.epics())
					{
						for (FeatureRequest featureRequest : epic.featureRequests())
						{
							// add initiatives that have epics that have feature requests that match predicate
							if (config.predicateFeatureRequest().get().test(featureRequest))
							{
								Initiative newInitiative = newInitiativesByKey.get(epic.initiative().recordGroup().lastRecord().key());

								if (newInitiative == null)
								{
									newInitiative = new Initiative(epic.initiative().recordGroup());
									newInitiativesByKey.put(newInitiative.lastRecord().key(), newInitiative);
								}

								Epic newEpic = newEpicsByKey.get(epic.lastRecord().key());
								
								if (newEpic == null)
								{
									newEpic = new Epic(epic.recordGroup(), newInitiative);
									newEpicsByKey.put(newEpic.lastRecord().key(), newEpic);
									newInitiative.epics().add(newEpic);
								}
								
								FeatureRequest newFeatureRequest = newFeatureRequestsByKey.get(featureRequest.lastRecord().key());

								if (newFeatureRequest == null)
								{
									newFeatureRequest = new FeatureRequest(featureRequest.recordGroup(), newEpic);
									newFeatureRequestsByKey.put(newFeatureRequest.lastRecord().key(), newFeatureRequest);
									newEpic.featureRequests().add(newFeatureRequest);
								}

								result.add(newInitiative);
							}
						}
					}
				}
			}

			return result;
		}		
	}
	
	static HierarchyQuery create() { return new HierarchyQuerySimple(); }
}