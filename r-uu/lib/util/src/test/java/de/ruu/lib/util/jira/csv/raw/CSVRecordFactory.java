package de.ruu.lib.util.jira.csv.raw;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVRecordFactory
{
	/**
	 * Jira exports contain multiple data rows as csv strings. Each string stores data of an {@link
	 * CSVRecord}.
	 *
	 * @param csvDataRows       see above
	 * @param optionalPredicate see below
	 * @return {@link CSVRecord}s by key, each {@link CSVRecord} matches {@code
	 *         optionalPredicate} if predicate is available
	 */
	public static Map<String, TreeSet<CSVRecord>> recordsByKey(
			@NonNull List<String>                   csvDataRows,
			@NonNull Optional<Predicate<CSVRecord>> optionalPredicate)
	{
//		log.debug("processing {} data rows", csvDataRows.size());

		Map<String, TreeSet<CSVRecord>> result = new TreeMap<>();
		Predicate          <CSVRecord>  predicate;

		if (optionalPredicate.isPresent()) predicate = optionalPredicate.get();
		else                               predicate = record -> true; // no records will be rejected

		for (String csvDataRow : csvDataRows)
		{
			CSVRecord record = new CSVRecord(csvDataRow); // build record from row / string

			if (predicate.test(record)) // check predicate
			{
				// lookup set of records for record's key in result
				TreeSet<CSVRecord> recordSet = result.get(record.key());

				if (recordSet == null)
				{
					// no record set for record's key yet, create new set and add it to result
					recordSet = new TreeSet<>(CSVRecord.comparator());
					result.put(record.key(), recordSet);
				}

				if (recordSet.add(record) == false)
				{
					log.error("record already existed in set\n{}", record.asCSVRow());
				}
			}
			else
			{
				log.warn("rejected\n{}", record.asCSVRow());
			}
		}

//		log.debug("processed  {} data rows"                    , csvDataRows.size());
//		log.debug("found      {} records"                      , countRecords(result));
//		log.debug("           {} rejected or duplicate records", rejectedAndDuplicateCount);

		return result;
	}
	
	public static Map<String, TreeSet<CSVRecord>> recordsByKey(@NonNull List<String> csvDataRows)
	{
		return recordsByKey(csvDataRows, Optional.empty());
	}

	public static int countRecords(Map<String, TreeSet<CSVRecord>> recordsByKey)
	{
		int result = 0;
		for (String key : recordsByKey.keySet())
		{
			result += recordsByKey.get(key).size();
		}
		return result;
	}
}