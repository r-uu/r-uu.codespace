package de.ruu.lib.util.jira.csv.issue.hierarchy.query;

import java.util.Optional;
import java.util.function.Predicate;

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
import lombok.experimental.Accessors;

/**
 * {@link HierarchyQueryConfig}s consist of a collection of optional predicates each describing the
 * conditions under that the hierarchy object in question belongs to the result of the hierarchy
 * query. A non empty optional predicate indicates that the respective hierarchy object has to be
 * tested against its predicate to determine if it must be part of the query result or not. 
 */
public interface HierarchyQueryConfig
{
	@FunctionalInterface interface PredicateInitiative     extends Predicate<Initiative>
	{
		boolean test(Initiative     issueType);
	}
	@FunctionalInterface interface PredicateEpic           extends Predicate<Epic>
	{
		boolean test(Epic           issueType);
	}
	@FunctionalInterface interface PredicateStory          extends Predicate<Story>
	{
		boolean test(Story          issueType);
	}
	@FunctionalInterface interface PredicateSubTaskEpic    extends Predicate<SubTaskEpic>
	{
		boolean test(SubTaskEpic    issueType);
	}
	@FunctionalInterface interface PredicateSubTaskStory   extends Predicate<SubTaskStory>
	{
		boolean test(SubTaskStory   issueType);
	}
	@FunctionalInterface interface PredicateTask           extends Predicate<Task>
	{
		boolean test(Task           issueType);
	}
	@FunctionalInterface interface PredicateTechnicalTask  extends Predicate<TechnicalTask>
	{
		boolean test(TechnicalTask  issueType);
	}
	@FunctionalInterface interface PredicateBug            extends Predicate<Bug>
	{
		boolean test(Bug            issueType);
	}
	@FunctionalInterface interface PredicateFeatureRequest extends Predicate<FeatureRequest>
	{
		boolean test(FeatureRequest issueType);
	}

	default Optional<PredicateInitiative    > predicateInitiative    () { return Optional.empty(); }
	default Optional<PredicateEpic          > predicateEpic          () { return Optional.empty(); }
	default Optional<PredicateStory         > predicateStory         () { return Optional.empty(); }
	default Optional<PredicateSubTaskEpic   > predicateSubTaskEpic   () { return Optional.empty(); }
	default Optional<PredicateSubTaskStory  > predicateSubTaskStory  () { return Optional.empty(); }
	default Optional<PredicateTask          > predicateTask          () { return Optional.empty(); }
	default Optional<PredicateTechnicalTask > predicateTechnicalTask () { return Optional.empty(); }
	default Optional<PredicateBug           > predicateBug           () { return Optional.empty(); }
	default Optional<PredicateFeatureRequest> predicateFeatureRequest() { return Optional.empty(); }

	HierarchyQueryConfig predicateInitiative    (PredicateInitiative     predicate);
	HierarchyQueryConfig predicateEpic          (PredicateEpic           predicate);
	HierarchyQueryConfig predicateStory         (PredicateStory          predicate);
	HierarchyQueryConfig predicateSubTaskEpic   (PredicateSubTaskEpic    predicate);
	HierarchyQueryConfig predicateSubTaskStory  (PredicateSubTaskStory   predicate);
	HierarchyQueryConfig predicateTask          (PredicateTask           predicate);
	HierarchyQueryConfig predicateTechnicalTask (PredicateTechnicalTask  predicate);
	HierarchyQueryConfig predicateBug           (PredicateBug            predicate);
	HierarchyQueryConfig predicateFeatureRequest(PredicateFeatureRequest predicate);
	
	@Getter @Accessors(fluent = true)
	class HierarchyQueryConfigSimple implements HierarchyQueryConfig
	{
		private Optional<PredicateInitiative    > predicateInitiative     = HierarchyQueryConfig.super.predicateInitiative    ();
		private Optional<PredicateEpic          > predicateEpic           = HierarchyQueryConfig.super.predicateEpic          ();
		private Optional<PredicateStory         > predicateStory          = HierarchyQueryConfig.super.predicateStory         ();
		private Optional<PredicateSubTaskEpic   > predicateSubTaskEpic    = HierarchyQueryConfig.super.predicateSubTaskEpic   ();
		private Optional<PredicateSubTaskStory  > predicateSubTaskStory   = HierarchyQueryConfig.super.predicateSubTaskStory  ();
		private Optional<PredicateTask          > predicateTask           = HierarchyQueryConfig.super.predicateTask          ();
		private Optional<PredicateTechnicalTask > predicateTechnicalTask  = HierarchyQueryConfig.super.predicateTechnicalTask ();
		private Optional<PredicateBug           > predicateBug            = HierarchyQueryConfig.super.predicateBug           ();
		private Optional<PredicateFeatureRequest> predicateFeatureRequest = HierarchyQueryConfig.super.predicateFeatureRequest();

		@Override public HierarchyQueryConfig predicateInitiative(PredicateInitiative predicate)
		{
			predicateInitiative     = Optional.ofNullable(predicate);
			return this;
		}
		@Override public HierarchyQueryConfig predicateEpic(PredicateEpic predicate)
		{
			predicateEpic           = Optional.ofNullable(predicate);
			return this;
		}
		@Override public HierarchyQueryConfig predicateStory(PredicateStory predicate)
		{
			predicateStory          = Optional.ofNullable(predicate);
			return this;
		}
		@Override public HierarchyQueryConfig predicateSubTaskEpic(PredicateSubTaskEpic predicate)
		{
			predicateSubTaskEpic    = Optional.ofNullable(predicate);
			return this;
		}
		@Override public HierarchyQueryConfig predicateSubTaskStory(PredicateSubTaskStory predicate)
		{
			predicateSubTaskStory   = Optional.ofNullable(predicate);
			return this;
		}
		@Override public HierarchyQueryConfig predicateTask(PredicateTask predicate)
		{
			predicateTask           = Optional.ofNullable(predicate);
			return this;
		}
		@Override public HierarchyQueryConfig predicateTechnicalTask(PredicateTechnicalTask predicate)
		{
			predicateTechnicalTask  = Optional.ofNullable(predicate);
			return this;
		}
		@Override public HierarchyQueryConfig predicateBug(PredicateBug predicate)
		{
			predicateBug            = Optional.ofNullable(predicate);
			return this;
		}
		@Override public HierarchyQueryConfig predicateFeatureRequest(PredicateFeatureRequest predicate)
		{
			predicateFeatureRequest = Optional.ofNullable(predicate);
			return this;
		}
	}

	static HierarchyQueryConfig create() { return new HierarchyQueryConfigSimple(); }
}