package de.ruu.app.jeeeraaah.common.api.mapping.bean.dto;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-01T15:21:25+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_Task_Bean_DTOImpl implements Map_Task_Bean_DTO {

    @Override
    public TaskDTO map(TaskBean in, ReferenceCycleTracking context) {
        TaskDTO target = context.get( in, TaskDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskDTO taskDTO = create( in, context );

        context.put( in, taskDTO );
        beforeMapping( in, taskDTO, context );

        taskDTO.setName( in.getName() );
        taskDTO.description( in.getDescription() );
        taskDTO.start( in.getStart() );
        taskDTO.end( in.getEnd() );
        taskDTO.closed( in.getClosed() );

        afterMapping( in, taskDTO, context );

        return taskDTO;
    }
}
