package de.ruu.app.jeeeraaah.common.api.mapping.lazy.bean;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-01T06:06:40+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_Task_Lazy_BeanImpl implements Map_Task_Lazy_Bean {

    @Override
    public TaskBean map(TaskGroupBean group, TaskLazy in) {
        if ( group == null && in == null ) {
            return null;
        }

        TaskBean taskBean = create( group, in );

        beforeMapping( in, taskBean );

        if ( group != null ) {
            taskBean.taskGroup( group );
            taskBean.setName( group.getName() );
            taskBean.description( group.getDescription() );
        }

        afterMapping( in, taskBean );

        return taskBean;
    }
}
