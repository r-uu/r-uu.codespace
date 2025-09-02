package de.ruu.lib.util.jira.csv.issue.hierarchy.query;

import static de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryPredicates.epicHasMatchingStory;
import static de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryPredicates.initiativeDescriptionContainsAtLeastOneOf;
import static de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryPredicates.initiativeHasMatchingEpic;
import static de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryPredicates.storyHasEffortConsumed;

import java.util.List;

import de.ruu.lib.util.jira.csv.issue.hierarchy.query.HierarchyQueryConfig.HierarchyQueryConfigSimple;

public interface HierarchyQueryConfigs
{
	static class StoriesWithEffortConsumed extends HierarchyQueryConfigSimple
	{
		public StoriesWithEffortConsumed()
		{
			PredicateStory      predicateStory      = storyHasEffortConsumed();
			PredicateEpic       predicateEpic       = epicHasMatchingStory(predicateStory);
			PredicateInitiative predicateInitiative = initiativeHasMatchingEpic(predicateEpic);

			predicateStory     (predicateStory);
			predicateEpic      (predicateEpic);
			predicateInitiative(predicateInitiative);
		}
	}
	
	static class InitiativesInReportPlanned extends HierarchyQueryConfigSimple
	{
		public InitiativesInReportPlanned()
		{
			PredicateInitiative predicateInitiative =
					initiativeDescriptionContainsAtLeastOneOf(
							List.of
							(
									"Geschäftspartner - Dublettenprüfung",
									"Geschäftspartner - Verschmelzen und Zusammenführen Geschäftspartner",
									"Schnittstellen - Nacharbeiten (RUMS Coexistenz GP RENEW -> RUMS)",
									"Schnittstellen - Online-Shop",
									"eANV Prozessintegration Ausbaustufe",
									"QS - Anlage und Änderungsinformation nachhalten",
									"Teilstorno für Fakturadokumente",
									"Subunternehmer",
									"Subunternehmer Integration",
									"Erweiterung Migrationssuite - Ausbaustufe 1 (GP)",
									"Erweiterung Migrationssuite - Ausbaustufe 2 (Vereinbarungen)",
									"Erweiterung Migrationssuite - Ausbaustufe 3 (Plantouren und Kundenportaleinstellungen)",
									"Veränderungen von LP und die Auswirkungen auf Vereinbarungen",
									"Vereinfachte Vereinbarungspflege",
									"Disposition und Verwiegungen im Behälterzug",
									"Integration RETRACK Behälterverwaltung",
									"Eingangsbelege kontrollieren",
									"Anbindung Kundenportal",
									"Vereinbarungskorrektur in Dispovorgängen",
									"Automatische Übertragung von RE-NEW und OBS",
									"Umgang mit 0 € Belegpositionen und Nicht-Fakturierbarkeit",
									"Subunternehmer Ausbaustufe 2",
									"Optimierung Beleglayout"
//								// ??? Erweiterung Automatische Ãœbertragung von RE-NEW und OBS
							));
			predicateInitiative(predicateInitiative);
		}
	}
}