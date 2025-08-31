package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.bean.TaskBean;
import de.ruu.app.jeeeraaah.common.bean.TaskGroupBean;
import de.ruu.app.jeeeraaah.common.dto.TaskLazy;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-31T16:34:58+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_Task_Lazy_BeanImpl implements Map_Task_Lazy_Bean {

    @Override
    public TaskBean map(TaskGroupBean groupBean, TaskLazy in) {
        if ( groupBean == null && in == null ) {
            return null;
        }

        TaskBean taskBean = create( groupBean, in );

        beforeMapping( groupBean, in, taskBean );

        if ( in != null ) {
            taskBean.description( in.getDescription() );
            taskBean.setName( in.getName() );
            taskBean.start( in.getStart() );
            taskBean.end( in.getEnd() );
            taskBean.closed( in.getClosed() );
        }

        afterMapping( groupBean, in, taskBean );

        return taskBean;
    }
}
