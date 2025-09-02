package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.fx.TaskFXBean;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-02T19:31:08+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_Task_Bean_FXBeanImpl implements Map_Task_Bean_FXBean {

    @Override
    public TaskFXBean map(TaskBean input, ReferenceCycleTracking context) {
        TaskFXBean target = context.get( input, TaskFXBean.class );
        if ( target != null ) {
            return target;
        }

        if ( input == null ) {
            return null;
        }

        TaskFXBean taskFXBean = create( input, context );

        context.put( input, taskFXBean );
        beforeMapping( input, taskFXBean, context );

        taskFXBean.setDescription( input.getDescription() );
        taskFXBean.setStart( input.getStart() );
        taskFXBean.setEnd( input.getEnd() );
        taskFXBean.setClosed( input.getClosed() );
        taskFXBean.setName( input.getName() );

        afterMapping( input, taskFXBean, context );

        return taskFXBean;
    }
}
