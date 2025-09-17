package de.ruu.app.jeeeraaah.frontend.ui.fx.task;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

import static java.util.Objects.isNull;

public class TreeCellFactory
{
    public static Callback<TreeView<TaskBean>, TreeCell<TaskBean>> cellFactory()
    {
        return new Callback<>()
        {
            @Override public TreeCell<TaskBean> call(TreeView<TaskBean> param)
            {
                return new TreeCell<>()
                {
                    @Override protected void updateItem(TaskBean item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (isNull(item) || empty) setText(null);
                        else                       setText(item.name());
                        setGraphic(null);
                    }
                };
            }
        };
    }
}