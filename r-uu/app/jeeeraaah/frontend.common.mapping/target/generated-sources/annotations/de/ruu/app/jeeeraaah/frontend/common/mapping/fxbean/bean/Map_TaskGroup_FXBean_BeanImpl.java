package de.ruu.app.jeeeraaah.frontend.common.mapping.fxbean.bean;

import de.ruu.app.jeeeraaah.common.api.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.frontend.ui.fx.model.TaskGroupFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-26T18:57:22+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_TaskGroup_FXBean_BeanImpl implements Map_TaskGroup_FXBean_Bean {

    @Override
    public TaskGroupBean map(TaskGroupFXBean in, ReferenceCycleTracking context) {
        TaskGroupBean target = context.get( in, TaskGroupBean.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskGroupBean taskGroupBean = create( in );

        context.put( in, taskGroupBean );
        beforeMapping( in, taskGroupBean, context );

        taskGroupBean.setName( in.getName() );
        taskGroupBean.description( in.getDescription() );

        afterMapping( in, taskGroupBean, context );

        return taskGroupBean;
    }
}
