package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-31T16:21:38+0000",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (GraalVM Community)"
)
public class Map_TaskGroup_EntityDTO_EntityJPAImpl implements Map_TaskGroup_EntityDTO_EntityJPA {

    @Override
    public TaskGroupEntityJPA map(TaskGroupEntityDTO in, ReferenceCycleTracking context) {
        TaskGroupEntityJPA target = context.get( in, TaskGroupEntityJPA.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskGroupEntityJPA taskGroupEntityJPA = create( in );

        context.put( in, taskGroupEntityJPA );
        beforeMapping( in, taskGroupEntityJPA, context );

        taskGroupEntityJPA.setName( in.getName() );
        taskGroupEntityJPA.description( in.getDescription() );

        afterMapping( in, taskGroupEntityJPA, context );

        return taskGroupEntityJPA;
    }
}
