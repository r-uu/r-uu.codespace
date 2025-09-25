package de.ruu.app.jeeeraaah.common.api.mapping.bean.lazy;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-25T17:01:55+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_Task_Bean_LazyImpl implements Map_Task_Bean_Lazy {

    @Override
    public TaskLazy map(TaskBean in) {
        if ( in == null ) {
            return null;
        }

        TaskLazy taskLazy = create( in );

        beforeMapping( in, taskLazy );

        taskLazy.description( in.getDescription() );
        taskLazy.name( in.getName() );
        taskLazy.start( in.getStart() );
        taskLazy.end( in.getEnd() );
        taskLazy.closed( in.getClosed() );

        afterMapping( in, taskLazy );

        return taskLazy;
    }
}
