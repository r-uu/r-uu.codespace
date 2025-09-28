package de.ruu.app.jeeeraaah.backend.common.mapping.dto.jpa;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-28T23:05:41+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_TaskGroup_DTO_JPAImpl implements Map_TaskGroup_DTO_JPA {

    @Override
    public TaskGroupJPA map(TaskGroupDTO in, ReferenceCycleTracking context) {
        TaskGroupJPA target = context.get( in, TaskGroupJPA.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskGroupJPA taskGroupJPA = create( in );

        context.put( in, taskGroupJPA );
        beforeMapping( in, taskGroupJPA, context );

        taskGroupJPA.setName( in.getName() );
        taskGroupJPA.description( in.getDescription() );

        afterMapping( in, taskGroupJPA, context );

        return taskGroupJPA;
    }
}
