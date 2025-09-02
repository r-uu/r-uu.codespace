package de.ruu.lib.util.jira.csv.raw;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

class CSVRecordTest
{
	@Test void testEquals()
	{
		CSVRecord record1 = new CSVRecord("2024.04.01;Version 2.2.1;RENEW-47891;Unteraufgabe;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;");
		CSVRecord record2 = new CSVRecord("2024.04.01;Version 2.2.1;RENEW-47891;Unteraufgabe;Fertig;Dev Merge;0;0;0;;RENEW-47886;;;;;");
		
		assertThat(record1.equals(record2), Is.is(true));
	}
}