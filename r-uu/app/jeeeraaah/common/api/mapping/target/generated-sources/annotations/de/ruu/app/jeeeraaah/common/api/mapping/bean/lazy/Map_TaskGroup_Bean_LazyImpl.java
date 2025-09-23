package de.ruu.app.jeeeraaah.common.api.mapping.bean.lazy;

import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T19:19:07+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_TaskGroup_Bean_LazyImpl implements Map_TaskGroup_Bean_Lazy {

    @Override
    public TaskGroupLazy map(TaskGroupBean in) {
        if ( in == null ) {
            return null;
        }

        TaskGroupLazy taskGroupLazy = create( in );

        beforeMapping( in, taskGroupLazy );

        afterMapping( in, taskGroupLazy );

        return taskGroupLazy;
    }
}
