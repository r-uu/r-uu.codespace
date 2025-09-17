package de.ruu.app.jeeeraaah.backend.persistence.jpa;

import java.util.ArrayList;
import java.util.List;

public class JPAFactory
{
    public static List<TaskGroupJPA> createTaskGroupJPAsWithNames(String ... names)
    {
        List<TaskGroupJPA> result = new ArrayList<>();
        for (String name : names) { result.add(new TaskGroupJPA(name)); }
        return result;
    }
    public static List<TaskJPA> createTaskJPAsForGroupWithNames(TaskGroupJPA group, String ... names)
    {
        List<TaskJPA> result = new ArrayList<>();
        for (String name : names) { result.add(new TaskJPA(group, name)); }
        return result;
    }
}