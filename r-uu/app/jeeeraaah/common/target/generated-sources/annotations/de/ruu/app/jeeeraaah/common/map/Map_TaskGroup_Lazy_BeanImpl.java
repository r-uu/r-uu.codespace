package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupLazy;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-31T16:05:14+0000",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (GraalVM Community)"
)
public class Map_TaskGroup_Lazy_BeanImpl implements Map_TaskGroup_Lazy_Bean {

    @Override
    public TaskGroupBean map(TaskGroupLazy in) {
        if ( in == null ) {
            return null;
        }

        TaskGroupBean taskGroupBean = create( in );

        beforeMapping( in, taskGroupBean );

        taskGroupBean.name( in.getName() );

        afterMapping( in, taskGroupBean );

        return taskGroupBean;
    }
}
