package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-07T07:07:48+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_Task_EntityDTO_EntityJPAImpl implements Map_Task_EntityDTO_EntityJPA {

    @Override
    public TaskEntityJPA map(TaskEntityDTO in, ReferenceCycleTracking context) {
        TaskEntityJPA target = context.get( in, TaskEntityJPA.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskEntityJPA taskEntityJPA = create( in, context );

        context.put( in, taskEntityJPA );
        beforeMapping( in, taskEntityJPA, context );

        taskEntityJPA.setName( in.getName() );
        taskEntityJPA.superTask( map( in.getSuperTask(), context ) );
        taskEntityJPA.description( in.getDescription() );
        taskEntityJPA.start( in.getStart() );
        taskEntityJPA.end( in.getEnd() );
        taskEntityJPA.closed( in.getClosed() );

        afterMapping( in, taskEntityJPA, context );

        return taskEntityJPA;
    }
}
