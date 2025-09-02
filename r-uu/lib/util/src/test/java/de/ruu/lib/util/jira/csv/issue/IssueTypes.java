package de.ruu.lib.util.jira.csv.issue;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import de.ruu.lib.util.jira.csv.raw.CSVRecord;
import de.ruu.lib.util.jira.csv.raw.CSVRecordGroup;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

public interface IssueTypes
{
	@Getter @Accessors(fluent = true)
	@RequiredArgsConstructor
	@EqualsAndHashCode
	public abstract class IssueTypeAbstract implements Comparable<IssueTypeAbstract>
	{
		@NonNull private final CSVRecordGroup recordGroup;

		@Override public int compareTo(@NonNull IssueTypeAbstract o)
		{
			return lastRecord().compareTo(o.recordGroup.records().last());
		}

		public @NonNull CSVRecord lastRecord() { return recordGroup.records().last(); }

		public static Comparator<IssueTypeAbstract> comparator() { return (ita1, ita2) -> ita1.compareTo(ita2); }
	}

	@Getter @Accessors(fluent = true)
	public class Initiative extends IssueTypeAbstract
	{
//		private final static Initiative SYNTHETIC_INSTANCE = new Initiative(CSVRecord.createSynthetic(IssueType.Initiative));
//		public  final static Initiative syntheticInstance() { return SYNTHETIC_INSTANCE; }

		private final Set<Epic> epics = new TreeSet<>();

		public Initiative(@NonNull CSVRecordGroup recordGroup) { super(recordGroup); }
		
//		public static Set<Epic> epicsOfSyntheticInstance() { return Collections.unmodifiableSet(SYNTHETIC_INSTANCE.epics); }
	}

	@Getter @Accessors(fluent = true)
	public class Epic extends IssueTypeAbstract
	{
//		private final static Epic SYNTHETIC_INSTANCE = new Epic(CSVRecord.createSynthetic(IssueType.Epic), Initiative.syntheticInstance());
//		public  final static Epic syntheticInstance() { return SYNTHETIC_INSTANCE; }

		@NonNull private final Initiative initiative;
	
		private final Set<Story>          stories         = new TreeSet<>();
		private final Set<TechnicalTask>  technicalTasks  = new TreeSet<>();
		private final Set<Task>           tasks           = new TreeSet<>();
		private final Set<Bug>            bugs            = new TreeSet<>();
		private final Set<FeatureRequest> featureRequests = new TreeSet<>();
		private final Set<SubTaskEpic>    subTasks        = new TreeSet<>();		
	
		public Epic(@NonNull CSVRecordGroup recordGroup, @NonNull Initiative initiative)
		{
			super(recordGroup);
			this.initiative = initiative;
			initiative.epics().add(this);
		}
	}

	@Getter @Accessors(fluent = true)
	public class Story extends IssueTypeAbstract
	{
//		private final static Story SYNTHETIC_INSTANCE = new Story(CSVRecord.createSynthetic(IssueType.Epic), Epic.syntheticInstance());
//		public  final static Story syntheticInstance() { return SYNTHETIC_INSTANCE; }
		
		@NonNull private final Epic epic;
	
		private final Set<SubTaskStory> subTasks = new TreeSet<>();
	
		public Story(@NonNull CSVRecordGroup recordGroup, @NonNull Epic epic)
		{
			super(recordGroup);
			this.epic = epic;
			epic.stories().add(this);
		}
	}

	@Getter @Accessors(fluent = true)
	public class SubTaskStory extends IssueTypeAbstract
	{
		@NonNull private final Story story;
	
		public SubTaskStory(@NonNull CSVRecordGroup recordGroup, @NonNull Story story)
		{
			super(recordGroup);
			this.story = story;
			story.subTasks().add(this);
		}
	}

	@Getter @Accessors(fluent = true)
	public class SubTaskEpic extends IssueTypeAbstract
	{
		@NonNull private final Epic epic;
	
		public SubTaskEpic(@NonNull CSVRecordGroup recordGroup, @NonNull Epic epic)
		{
			super(recordGroup);
			this.epic = epic;
			epic.subTasks().add(this);
		}
	}

	@Getter @Accessors(fluent = true)
	public class Task extends IssueTypeAbstract
	{
		@NonNull private final Epic epic;
	
		public Task(@NonNull CSVRecordGroup recordGroup, @NonNull Epic epic)
		{
			super(recordGroup);
			this.epic = epic;
			epic.tasks().add(this);
		}
	}

	@Getter @Accessors(fluent = true)
	public class TechnicalTask extends IssueTypeAbstract
	{
		@NonNull private final Epic epic;
	
		public TechnicalTask(@NonNull CSVRecordGroup recordGroup, @NonNull Epic epic)
		{
			super(recordGroup);
			this.epic = epic;
			epic.technicalTasks().add(this);
		}
	}

	@Getter @Accessors(fluent = true)
	public class Bug extends IssueTypeAbstract
	{
		@NonNull private final Epic epic;
	
		public Bug(@NonNull CSVRecordGroup recordGroup, @NonNull Epic epic)
		{
			super(recordGroup);
			this.epic = epic;
			epic.bugs().add(this);
		}
	}

	@Getter @Accessors(fluent = true)
	public class FeatureRequest extends IssueTypeAbstract
	{
		@NonNull private final Epic epic;
	
		public FeatureRequest(@NonNull CSVRecordGroup recordGroup, @NonNull Epic epic)
		{
			super(recordGroup);
			this.epic = epic;
			epic.featureRequests().add(this);
		}
	}
}