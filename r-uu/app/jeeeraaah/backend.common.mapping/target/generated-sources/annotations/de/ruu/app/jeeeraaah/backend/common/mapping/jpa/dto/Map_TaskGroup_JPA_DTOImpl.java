package de.ruu.app.jeeeraaah.backend.common.mapping.jpa.dto;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskGroupDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-01T06:28:51+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_TaskGroup_JPA_DTOImpl implements Map_TaskGroup_JPA_DTO {

    @Override
    public TaskGroupDTO map(TaskGroupJPA in, ReferenceCycleTracking context) {
        TaskGroupDTO target = context.get( in, TaskGroupDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskGroupDTO taskGroupDTO = create( in );

        context.put( in, taskGroupDTO );
        beforeMapping( in, taskGroupDTO, context );

        taskGroupDTO.setName( in.getName() );
        taskGroupDTO.description( in.getDescription() );

        afterMapping( in, taskGroupDTO, context );

        return taskGroupDTO;
    }
}
