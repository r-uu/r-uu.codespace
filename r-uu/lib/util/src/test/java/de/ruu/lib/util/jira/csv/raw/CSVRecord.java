package de.ruu.lib.util.jira.csv.raw;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.ruu.lib.util.jira.csv.issue.IssueType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 *    {@link CSVRecord}s represent a raw jira export data row with 16 cells in excel csv export format.
 * <p>Cell values have non-string types where appropriate.
 * <p>{@link CSVRecord}s support {@link Comparable} via {@link #compareTo(CSVRecord)}.
 */
@Getter @Accessors(fluent = true)
@EqualsAndHashCode
public class CSVRecord implements Comparable<CSVRecord>
{
	/**
	 * {@link CSVRecord} can be stored in a {@link CSVRecordGroup}. Within a {@link CSVRecordGroup} each {@link CSVRecord} has
	 * to be unique with regard to {@link CSVRecord#groupId}. {@link CSVRecordGroupItemId} is composed from {@link #key} and
	 * {@link #date} which means, that it is allowed to put multiple {@link CSVRecord}s with the same {@link #key} in a
	 * {@link CSVRecordGroup} as long as they all differ with regard to {@link #date}.
	 */
	@Getter @Accessors(fluent = true)
	@EqualsAndHashCode
	public static class CSVRecordGroupItemId implements Comparable<CSVRecordGroupItemId>
	{
		@NonNull private final String    key;
		@NonNull private final LocalDate date;
		
		public CSVRecordGroupItemId(@NonNull CSVRecord record)
		{
			key  = record.key;
			date = record.date;
		}

		public static Comparator<CSVRecordGroupItemId> comparator()
		{
			return
					(id1, id2) ->
					{
						int result = id1.key.compareTo(id2.key);
						if (result != 0)
								return result;
						result = id1.date.compareTo(id2.date);
						return result;
					};
		}

		@Override public int compareTo(CSVRecordGroupItemId other) { return comparator().compare(this, other); }
	}

	private final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

	@NonNull private LocalDate    date;
	@NonNull private String       fixVersion;
	@NonNull private String       key;
	@NonNull private IssueType    type;
	@NonNull private String       status;
	@NonNull private String       description;
	@NonNull private Float        effortEstimated;
	@NonNull private Float        effortRemaining;
	@NonNull private Float        effortConsumed;
	@NonNull private String       f6;
	@NonNull private String       parentKey;
	@NonNull private List<String> components;
	@NonNull private List<String> keyWords;
	@NonNull private String       epicLink;
	@NonNull private String       parentId;
	@NonNull private String       flagged;
	
	@NonNull private CSVRecordGroupItemId groupId;

	public final static String SYNTHETIC = "SYNTHETIC";

	private CSVRecord(IssueType issueType)
	{
		date            = LocalDate.MIN;
		fixVersion      = SYNTHETIC;
		key             = SYNTHETIC;
		type            = issueType;
		status          = SYNTHETIC;
		description     = SYNTHETIC;
		effortEstimated = 0F;
		effortRemaining = 0F;
		effortConsumed  = 0F;
		f6              = SYNTHETIC;
		parentKey       = SYNTHETIC;
		components      = Collections.emptyList();
		keyWords        = Collections.emptyList();
		epicLink        = SYNTHETIC;
		parentId        = SYNTHETIC;
		flagged         = SYNTHETIC;
		
		groupId = new CSVRecordGroupItemId(this);
	}
	
	public final static CSVRecord createSynthetic(IssueType type) { return new CSVRecord(type); }

	public CSVRecord(@NonNull String data)
	{
		String[] splitValues = data.split(";");
		String[] cellValues  = Arrays.copyOf(splitValues, 16); // make sure we have exactly 16 cells

		int i = -1;

		date            = asDate      (asString(cellValues[++i]));
		fixVersion      =              asString(cellValues[++i]);
		key             =              asString(cellValues[++i]);
		type            = asIssueType (asString(cellValues[++i]));
		status          =              asString(cellValues[++i]);
		description     =              asString(cellValues[++i]);
		effortEstimated = asFloat     (asString(cellValues[++i]));
		effortRemaining = asFloat     (asString(cellValues[++i]));
		effortConsumed  = asFloat     (asString(cellValues[++i]));
		f6              =              asString(cellValues[++i]);
		parentKey       =              asString(cellValues[++i]);
		components      = asStringList(asString(cellValues[++i]));
		keyWords        = asStringList(asString(cellValues[++i]));
		epicLink        =              asString(cellValues[++i]);
		parentId        =              asString(cellValues[++i]);
		flagged         =              asString(cellValues[++i]);
		
		groupId = new CSVRecordGroupItemId(this);
	}

	public static Comparator<CSVRecord> comparator() { return (r1, r2) -> r1.groupId.compareTo(r2.groupId); }

	@Override public int compareTo(CSVRecord o) { return comparator().compare(this, o); }
	
	public String asCSVRow()
	{
		return
				""
				+       asString        (date)
				+ ";" +                  fixVersion
				+ ";" +                  key
				+ ";" +                  type.name()
				+ ";" +                  status
				+ ";" +                  description
				+ ";" +                  effortEstimated.toString().replace('.', ',')
				+ ";" +                  effortRemaining.toString().replace('.', ',')
				+ ";" +                  effortConsumed .toString().replace('.', ',')
				+ ";" +                  f6
				+ ";" +                  parentKey
				+ ";" + String.join(",", components)
				+ ";" + String.join(",", keyWords)
				+ ";" +                  epicLink
				+ ";" +                  parentId
				+ ";" +                  flagged
				;
	}

	private String asString(String string)
	{
		if (string == null) return "";
		return string;
	}

	private String asString(LocalDate date)
	{
		if (date == null) return "";
		return DATE_TIME_FORMATTER.format(date);
	}

	private IssueType asIssueType(@NonNull String issueType)
	{
		return IssueType.issueType(issueType);
	}

	private @NonNull LocalDate asDate(@NonNull String string)
	{
		return LocalDate.parse(string, DATE_TIME_FORMATTER);
	}

	private @NonNull Float asFloat(String string)
	{
		if (string == null) return 0F;
		if (string.isEmpty()) return 0F;
		return Float.parseFloat(string.replaceAll("\"", "").replace(',', '.'));
	}

	private @NonNull List<String> asStringList(String string)
	{
		if (string == null) return Collections.emptyList();
		return Arrays.asList(string.split(","));
	}
}