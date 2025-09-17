package de.ruu.app.jeeeraaah.frontend.ui.fx.task;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import javafx.scene.control.TreeItem;

public class TreeItemTaskBean extends TreeItem<TaskBean>
{
    public TreeItemTaskBean(TaskBean task) { super(task); }
    @Override public String toString() { return getValue().name(); }
}