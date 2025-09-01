package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-01T08:49:24+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_TaskGroup_EntityJPA_EntityDTOImpl implements Map_TaskGroup_EntityJPA_EntityDTO {

    @Override
    public TaskGroupEntityDTO map(TaskGroupEntityJPA in, ReferenceCycleTracking context) {
        TaskGroupEntityDTO target = context.get( in, TaskGroupEntityDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskGroupEntityDTO taskGroupEntityDTO = create( in );

        context.put( in, taskGroupEntityDTO );
        beforeMapping( in, taskGroupEntityDTO, context );

        taskGroupEntityDTO.setName( in.getName() );
        taskGroupEntityDTO.description( in.getDescription() );

        afterMapping( in, taskGroupEntityDTO, context );

        return taskGroupEntityDTO;
    }
}
