package de.ruu.app.jeeeraaah.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Accessors(fluent = true)
@ToString
public class InterTaskRelationData
{
	@NonNull private Long id;
	@NonNull private Long idRelated;
}