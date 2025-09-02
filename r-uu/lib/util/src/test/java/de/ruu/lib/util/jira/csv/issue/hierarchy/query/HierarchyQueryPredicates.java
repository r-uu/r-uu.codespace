package de.ruu.lib.util.jira.csv.issue.hierarchy.query;

import java.util.List;
import java.util.function.Predicate;

import de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryConfig.PredicateEpic;
import de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryConfig.PredicateInitiative;
import de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryConfig.PredicateStory;
import lombok.NonNull;

public abstract class HierarchyQueryPredicates
{
	public static PredicateInitiative initiativeDescriptionContains(@NonNull CharSequence string)
	{
		return i -> i.lastRecord().description().contains(string);
	}
	public static PredicateInitiative initiativeDescriptionContainsAtLeastOneOf(@NonNull List<String> strings)
	{
		return i -> stringContainsAtLeastOneOf(i.lastRecord().description(), strings);
	}
	private static boolean stringContainsAtLeastOneOf(
			@NonNull String string, @NonNull List<String> strings)
	{
		Predicate<String> predicate = s -> strings.stream().anyMatch(item -> item.contains(s));
		return predicate.test(string);
	}
	public static PredicateInitiative initiativeHasMatchingEpic(@NonNull PredicateEpic predicate)
	{
		return i -> i.epics().stream().anyMatch(predicate);
	}
	public static PredicateEpic epicHasMatchingInitiative(@NonNull PredicateInitiative predicate)
	{
		return e -> predicate.test(e.initiative());
	}
	public static PredicateEpic epicHasMatchingStory(@NonNull PredicateStory predicate)
	{
		return e -> e.stories().stream().anyMatch(predicate);
	}
	public static PredicateStory storyHasEffortConsumed()
	{
		return
				s ->
				s.recordGroup().effortConsumedMin().effortConsumed() <
				s.recordGroup().effortConsumedMax().effortConsumed();
	}
}