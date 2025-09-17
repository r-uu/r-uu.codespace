package de.ruu.app.jeeeraaah.common.api.ws.rs;

import java.util.ArrayList;
import java.util.List;

public class DTOFactory
{
    public static List<TaskGroupDTO> createTaskGroupDTOsWithNames(String ... names)
    {
        List<TaskGroupDTO> result = new ArrayList<>();
        for (String name : names) { result.add(new TaskGroupDTO(name)); }
        return result;
    }
    public static List<TaskDTO> createTaskDTOsForGroupWithNames(TaskGroupDTO group, String ... names)
    {
        List<TaskDTO> result = new ArrayList<>();
        for (String name : names) { result.add(new TaskDTO(group, name)); }
        return result;
    }
}