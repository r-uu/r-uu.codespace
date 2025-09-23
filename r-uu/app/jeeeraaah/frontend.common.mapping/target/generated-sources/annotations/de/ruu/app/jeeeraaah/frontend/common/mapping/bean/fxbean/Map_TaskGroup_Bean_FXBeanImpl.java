package de.ruu.app.jeeeraaah.frontend.common.mapping.bean.fxbean;

import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.frontend.ui.fx.model.TaskGroupFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T19:19:50+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_TaskGroup_Bean_FXBeanImpl implements Map_TaskGroup_Bean_FXBean {

    @Override
    public TaskGroupFXBean map(TaskGroupBean in, ReferenceCycleTracking context) {
        TaskGroupFXBean target = context.get( in, TaskGroupFXBean.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskGroupFXBean taskGroupFXBean = create( in );

        context.put( in, taskGroupFXBean );
        beforeMapping( in, taskGroupFXBean, context );

        taskGroupFXBean.setName( in.getName() );
        taskGroupFXBean.setDescription( in.getDescription() );

        afterMapping( in, taskGroupFXBean, context );

        return taskGroupFXBean;
    }
}
