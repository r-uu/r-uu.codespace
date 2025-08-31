package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskGroupEntityDTO;
import de.ruu.lib.mapstruct.ReferenceCycleTracking;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-31T18:27:54+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_TaskGroup_EntityDTO_BeanImpl implements Map_TaskGroup_EntityDTO_Bean {

    @Override
    public TaskGroupBean map(TaskGroupEntityDTO in, ReferenceCycleTracking context) {
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

        taskGroupBean.name( in.getName() );
        taskGroupBean.description( in.getDescription() );

        afterMapping( in, taskGroupBean, context );

        return taskGroupBean;
    }
}
