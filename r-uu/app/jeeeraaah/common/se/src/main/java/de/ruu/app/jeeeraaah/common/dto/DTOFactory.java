package de.ruu.app.jeeeraaah.common.dto;

import java.util.ArrayList;
import java.util.List;

public class DTOFactory
{
    public static List<TaskGroupEntityDTO> createTaskGroupDTOsWithNames(String ... names)
    {
        List<TaskGroupEntityDTO> result = new ArrayList<>();
        for (String name : names) { result.add(new TaskGroupEntityDTO(name)); }
        return result;
    }
    public static List<TaskEntityDTO> createTaskDTOsForGroupWithNames(TaskGroupEntityDTO group, String ... names)
    {
        List<TaskEntityDTO> result = new ArrayList<>();
        for (String name : names) { result.add(new TaskEntityDTO(group, name)); }
        return result;
    }
}