package de.ruu.app.jeeeraaah.common.api.bean;

import java.util.ArrayList;
import java.util.List;

public class BeanFactory
{
    public static List<TaskGroupBean> createTaskGroupBeansWithNames(String ... names)
    {
        List<TaskGroupBean> result = new ArrayList<>();
        for (String name : names) { result.add(new TaskGroupBean(name)); }
        return result;
    }
    public static List<TaskBean> createTaskBeansForGroupWithNames(TaskGroupBean group, String ... names)
    {
        List<TaskBean> result = new ArrayList<>();
        for (String name : names) { result.add(new TaskBean(group, name)); }
        return result;
    }
}