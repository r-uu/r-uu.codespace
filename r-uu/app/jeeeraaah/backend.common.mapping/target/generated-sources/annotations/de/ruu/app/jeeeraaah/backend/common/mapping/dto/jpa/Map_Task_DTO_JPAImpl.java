package de.ruu.app.jeeeraaah.backend.common.mapping.dto.jpa;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-28T22:44:14+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_Task_DTO_JPAImpl implements Map_Task_DTO_JPA {

    @Override
    public TaskJPA map(TaskDTO in, ReferenceCycleTracking context) {
        TaskJPA target = context.get( in, TaskJPA.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskJPA taskJPA = create( in, context );

        context.put( in, taskJPA );
        beforeMapping( in, taskJPA, context );

        taskJPA.setName( in.getName() );
        taskJPA.superTask( map( in.getSuperTask(), context ) );
        taskJPA.description( in.getDescription() );
        taskJPA.start( in.getStart() );
        taskJPA.end( in.getEnd() );
        taskJPA.closed( in.getClosed() );

        afterMapping( in, taskJPA, context );

        return taskJPA;
    }
}
