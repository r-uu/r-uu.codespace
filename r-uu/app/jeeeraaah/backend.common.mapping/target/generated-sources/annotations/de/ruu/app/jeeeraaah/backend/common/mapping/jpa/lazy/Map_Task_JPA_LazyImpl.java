package de.ruu.app.jeeeraaah.backend.common.mapping.jpa.lazy;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA;
import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-28T23:05:41+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_Task_JPA_LazyImpl implements Map_Task_JPA_Lazy {

    @Override
    public TaskLazy map(TaskJPA in, ReferenceCycleTracking context) {
        TaskLazy target = context.get( in, TaskLazy.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskLazy taskLazy = create( in );

        context.put( in, taskLazy );
        beforeMapping( in, taskLazy, context );

        taskLazy.name( in.getName() );
        taskLazy.description( in.getDescription() );
        taskLazy.start( in.getStart() );
        taskLazy.end( in.getEnd() );
        taskLazy.closed( in.getClosed() );

        afterMapping( in, taskLazy, context );

        return taskLazy;
    }
}
