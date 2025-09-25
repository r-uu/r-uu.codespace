package de.ruu.app.jeeeraaah.frontend.common.mapping.fxbean.bean;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.frontend.ui.fx.model.TaskFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-24T07:00:18+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_Task_FXBean_BeanImpl implements Map_Task_FXBean_Bean {

    @Override
    public TaskBean map(TaskFXBean in, ReferenceCycleTracking context) {
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
        taskBean.description( in.getDescription() );
        taskBean.start( in.getStart() );
        taskBean.end( in.getEnd() );
        taskBean.closed( in.getClosed() );

        afterMapping( in, taskBean, context );

        return taskBean;
    }
}
