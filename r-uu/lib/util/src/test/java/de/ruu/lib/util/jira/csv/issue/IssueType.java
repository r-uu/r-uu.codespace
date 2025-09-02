package de.ruu.lib.util.jira.csv.issue;

import java.util.Comparator;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

/** allows categorisation of jira issue types, issue types are <b>not</b> aggregated or grouped in this type */
@Getter @Accessors(fluent = true)
public enum IssueType
{
	Initiative,
	Epic,
	Story,
	SubTask("Unteraufgabe"),
	Task("Aufgabe"),
	TechnicalTask("Technical Task"),
	Bug,
	FeatureRequest("Feature Request"),
	// Anforderung???
	;

	private String typeName;

	private IssueType(@NonNull String name) { typeName =      name;   }
	private IssueType()                     { typeName = this.name(); }

	public static IssueType issueType(@NonNull String typeName)
	{
		for (IssueType issueType : IssueType.values())
		{
			if (issueType.typeName.equals(typeName)) return issueType;
		}
		throw new IllegalArgumentException("no type for " + typeName);
	}

	public static Comparator<IssueType> compareByTypeName()
	{
		return (it1, it2) -> it1.typeName.compareTo(it2.typeName);
	}
}