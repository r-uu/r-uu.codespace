package de.ruu.app.jeeeraaah.frontend.common.mapping.bean.fxbean;

import de.ruu.app.jeeeraaah.common.api.bean.TaskBean;
import de.ruu.app.jeeeraaah.frontend.ui.fx.model.TaskFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-28T23:06:54+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_Task_Bean_FXBeanImpl implements Map_Task_Bean_FXBean {

    @Override
    public TaskFXBean map(TaskBean in, ReferenceCycleTracking context) {
        TaskFXBean target = context.get( in, TaskFXBean.class );
        if ( target != null ) {
            return target;
        }

        if ( in == null ) {
            return null;
        }

        TaskFXBean taskFXBean = create( in, context );

        context.put( in, taskFXBean );
        beforeMapping( in, taskFXBean, context );

        taskFXBean.setName( in.getName() );
        taskFXBean.setDescription( in.getDescription() );
        taskFXBean.setStart( in.getStart() );
        taskFXBean.setEnd( in.getEnd() );
        taskFXBean.setClosed( in.getClosed() );

        afterMapping( in, taskFXBean, context );

        return taskFXBean;
    }
}
