package de.ruu.lib.util.jira.csv.raw;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import de.ruu.lib.util.jira.csv.issue.IssueType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Getter @Accessors(fluent = true)
@EqualsAndHashCode
public class CSVRecordGroup implements Comparable<CSVRecordGroup>
{
	private final static TreeSet<CSVRecordGroup> SYNTHETIC_INITIATIVES = new TreeSet<>(comparator());
	private final static TreeSet<CSVRecordGroup> SYNTHETIC_EPICS       = new TreeSet<>(comparator());
	private final static TreeSet<CSVRecordGroup> SYNTHETIC_STORIES     = new TreeSet<>(comparator());

	/** common to all items in {@link #records()} */
	private final @NonNull String             key;
	@EqualsAndHashCode.Exclude
	private final @NonNull TreeSet<CSVRecord> records = new TreeSet<>(CSVRecord.comparator());

	public CSVRecordGroup(@NonNull TreeSet<CSVRecord> records)
	{
		if (records.isEmpty()) throw new IllegalArgumentException("records must not be empty");

		key = records.first().key();

		for (CSVRecord record : records)
		{
			if (record.key().equals(key) == false)
			{
				throw new IllegalArgumentException("key mismatch among records, key: " + key + " record.key " + record.key());
			}
		}

		this.records.addAll(records);
	}

	public CSVRecord effortConsumedMin()
	{
		return records.stream().min(Comparator.comparing(CSVRecord::effortConsumed)).orElseThrow(NoSuchElementException::new);
	}

	public CSVRecord effortConsumedMax()
	{
		return records.stream().max(Comparator.comparing(CSVRecord::effortConsumed)).orElseThrow(NoSuchElementException::new);
	}
	
	public float effortConsumed() { return effortConsumedMax().effortConsumed() - effortConsumedMin().effortConsumed(); }

	@Override public int compareTo(CSVRecordGroup o) { return comparator().compare(this, o); }
	
	/** @return issue type of last record defines issue type of group */
	public IssueType type() { return lastRecord().type(); }
	
	public CSVRecord lastRecord() { return records.last(); }

	public static Comparator<CSVRecordGroup> comparator() { return (g1, g2) -> g1.key.compareTo(g2.key); }
	
	public static void addSyntheticInitiatives(CSVRecordGroup recordGroup) { SYNTHETIC_INITIATIVES.add(recordGroup); }
	public static void addSyntheticEpics      (CSVRecordGroup recordGroup) { SYNTHETIC_EPICS      .add(recordGroup); }
	public static void addSyntheticStories    (CSVRecordGroup recordGroup) { SYNTHETIC_STORIES    .add(recordGroup); }
}