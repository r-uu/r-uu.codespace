package de.ruu.app.jeeeraaah.common.api.mapping.dto.bean;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.api.ws.rs.TaskDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-22T17:32:04+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_Task_DTO_BeanImpl implements Map_Task_DTO_Bean {

    @Override
    public TaskBean map(TaskDTO in, ReferenceCycleTracking context) {
        TaskBean target = context.get( in, TaskBean.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskBean taskBean = create( in, context );

        context.put( in, taskBean );
        beforeMapping( in, taskBean, context );

        taskBean.setName( in.getName() );
        taskBean.superTask( map( in.getSuperTask(), context ) );
        taskBean.description( in.getDescription() );
        taskBean.start( in.getStart() );
        taskBean.end( in.getEnd() );
        taskBean.closed( in.getClosed() );

        afterMapping( in, taskBean, context );

        return taskBean;
    }
}
