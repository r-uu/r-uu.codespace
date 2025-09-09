package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-09T07:26:00+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_Task_Bean_EntityDTOImpl implements Map_Task_Bean_EntityDTO {

    @Override
    public TaskEntityDTO map(TaskBean in, ReferenceCycleTracking context) {
        TaskEntityDTO target = context.get( in, TaskEntityDTO.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskEntityDTO taskEntityDTO = create( in, context );

        context.put( in, taskEntityDTO );
        beforeMapping( in, taskEntityDTO, context );

        taskEntityDTO.setName( in.getName() );
        taskEntityDTO.description( in.getDescription() );
        taskEntityDTO.start( in.getStart() );
        taskEntityDTO.end( in.getEnd() );
        taskEntityDTO.closed( in.getClosed() );

        afterMapping( in, taskEntityDTO, context );

        return taskEntityDTO;
    }
}
