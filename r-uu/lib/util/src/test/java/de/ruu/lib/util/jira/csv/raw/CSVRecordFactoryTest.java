package de.ruu.lib.util.jira.csv.raw;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeSet;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class CSVRecordFactoryTest
{
	private final List<String> csvDataRows =
			List.of
			(
  "2024.04.01;Version 2.2.1;RENEW-47891;Unteraufgabe;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
, "2024.04.11;Version 2.2.1;RENEW-47891;Unteraufgabe;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
, "2024.04.01;Version 2.2.1;RENEW-47892;Unteraufgabe;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
, "2024.04.11;Version 2.2.1;RENEW-47892;Unteraufgabe;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;"
			);

	@Test void testRecordsByKeyWithoutPredicate()
	{
		Map<String,TreeSet<CSVRecord>> recordsByKey = CSVRecordFactory.recordsByKey(csvDataRows, Optional.empty());

		assertThat(recordsByKey, Matchers.notNullValue());
		assertThat(recordsByKey.size(), Matchers.is(2));
		
		Iterator<Entry<String, TreeSet<CSVRecord>>> iterator = recordsByKey.entrySet().iterator();

		while (iterator.hasNext())
		{
			TreeSet<CSVRecord> csvRecords = iterator.next().getValue();

			assertThat(csvRecords, Matchers.notNullValue());
			assertThat(csvRecords.size(), Matchers.is(2));
		}
	}
}