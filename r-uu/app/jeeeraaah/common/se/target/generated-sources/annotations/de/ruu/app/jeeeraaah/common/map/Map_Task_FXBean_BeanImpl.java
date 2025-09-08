package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.fx.TaskFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-07T07:13:08+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_Task_FXBean_BeanImpl implements Map_Task_FXBean_Bean {

    @Override
    public TaskBean map(TaskFXBean input, ReferenceCycleTracking context) {
        TaskBean target = context.get( input, TaskBean.class );
        if ( target != null ) {
            return target;
        }

        if ( input == null ) {
            return null;
        }

        TaskBean taskBean = create( input, context );

        context.put( input, taskBean );
        beforeMapping( input, taskBean, context );

        taskBean.setName( input.getName() );
        taskBean.description( input.getDescription() );
        taskBean.start( input.getStart() );
        taskBean.end( input.getEnd() );
        taskBean.closed( input.getClosed() );

        afterMapping( input, taskBean, context );

        return taskBean;
    }
}
