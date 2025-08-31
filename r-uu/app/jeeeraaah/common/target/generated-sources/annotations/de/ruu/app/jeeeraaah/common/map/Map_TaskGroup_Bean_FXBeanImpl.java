package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.fx.TaskGroupFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-31T21:58:25+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_TaskGroup_Bean_FXBeanImpl implements Map_TaskGroup_Bean_FXBean {

    @Override
    public TaskGroupFXBean map(TaskGroupBean input, ReferenceCycleTracking context) {
        TaskGroupFXBean target = context.get( input, TaskGroupFXBean.class );
        if ( target != null ) {
            return target;
        }

        if ( input == null ) {
            return null;
        }

        TaskGroupFXBean taskGroupFXBean = create( input );

        context.put( input, taskGroupFXBean );
        beforeMapping( input, taskGroupFXBean, context );

        taskGroupFXBean.setDescription( input.getDescription() );

        afterMapping( input, taskGroupFXBean, context );

        return taskGroupFXBean;
    }
}
