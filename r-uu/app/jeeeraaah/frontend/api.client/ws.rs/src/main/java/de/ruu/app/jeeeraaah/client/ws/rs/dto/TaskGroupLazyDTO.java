package de.ruu.app.jeeeraaah.client.ws.rs.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

/**
 * Client-side DTO for task groups with the ids of their related tasks.
 * This is a simplified version of the server-side TaskGroupLazy class.
 */
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Accessors(fluent = true)
@NoArgsConstructor
@RequiredArgsConstructor
public class TaskGroupLazyDTO {
    @JsonProperty("id")
    @NonNull
    private Long id;
    
    @JsonProperty("name")
    @NonNull
    private String name;
    
    @JsonProperty("taskIds")
    @NonNull
    private Set<Long> taskIds = new HashSet<>();

    public void addTaskId(@NonNull Long taskId) {
        taskIds.add(taskId);
    }
}
