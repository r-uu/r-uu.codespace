package de.ruu.app.jeeeraaah.common.jpa;

import java.util.ArrayList;
import java.util.List;

public class JPAFactory
{
    public static List<TaskGroupEntityJPA> createTaskGroupJPAsWithNames(String ... names)
    {
        List<TaskGroupEntityJPA> result = new ArrayList<>();
        for (String name : names) { result.add(new TaskGroupEntityJPA(name)); }
        return result;
    }
    public static List<TaskEntityJPA> createTaskJPAsForGroupWithNames(TaskGroupEntityJPA group, String ... names)
    {
        List<TaskEntityJPA> result = new ArrayList<>();
        for (String name : names) { result.add(new TaskEntityJPA(group, name)); }
        return result;
    }
}