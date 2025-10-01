package de.ruu.app.jeeeraaah.backend.common.mapping.jpa.lazy;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-01T15:21:33+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_TaskGroup_JPA_LazyImpl implements Map_TaskGroup_JPA_Lazy {

    @Override
    public TaskGroupLazy map(TaskGroupJPA in, ReferenceCycleTracking context) {
        TaskGroupLazy target = context.get( in, TaskGroupLazy.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskGroupLazy taskGroupLazy = create( in );

        context.put( in, taskGroupLazy );
        beforeMapping( in, taskGroupLazy, context );

        taskGroupLazy.name( in.getName() );
        taskGroupLazy.description( in.getDescription() );

        afterMapping( in, taskGroupLazy, context );

        return taskGroupLazy;
    }
}
