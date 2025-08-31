package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.dto.TaskEntityDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-31T21:58:25+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_Task_EntityDTO_BeanImpl implements Map_Task_EntityDTO_Bean {

    @Override
    public TaskBean map(TaskEntityDTO in, ReferenceCycleTracking context) {
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
