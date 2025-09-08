package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.fx.TaskGroupFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-07T07:13:09+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_TaskGroup_FXBean_BeanImpl implements Map_TaskGroup_FXBean_Bean {

    @Override
    public TaskGroupBean map(TaskGroupFXBean input, ReferenceCycleTracking context) {
        TaskGroupBean target = context.get( input, TaskGroupBean.class );
        if ( target != null ) {
            return target;
        }

        if ( input == null ) {
            return null;
        }

        TaskGroupBean taskGroupBean = create( input );

        context.put( input, taskGroupBean );
        beforeMapping( input, taskGroupBean, context );

        taskGroupBean.name( input.getName() );
        taskGroupBean.description( input.getDescription() );

        afterMapping( input, taskGroupBean, context );

        return taskGroupBean;
    }
}
