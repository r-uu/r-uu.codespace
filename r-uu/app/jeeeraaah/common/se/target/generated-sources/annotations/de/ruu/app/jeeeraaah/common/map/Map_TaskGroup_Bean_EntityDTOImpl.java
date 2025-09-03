package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-03T09:57:59+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_TaskGroup_Bean_EntityDTOImpl implements Map_TaskGroup_Bean_EntityDTO {

    @Override
    public TaskGroupEntityDTO map(TaskGroupBean in, ReferenceCycleTracking context) {
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

        taskGroupEntityDTO.description( in.getDescription() );

        afterMapping( in, taskGroupEntityDTO, context );

        return taskGroupEntityDTO;
    }
}
