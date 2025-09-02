package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.dto.TaskLazy;
import de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-02T19:31:08+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_Task_EntityJPA_LazyImpl implements Map_Task_EntityJPA_Lazy {

    @Override
    public TaskLazy map(TaskEntityJPA in) {
        if ( in == null ) {
            return null;
        }

        TaskLazy taskLazy = new TaskLazy();

        beforeMapping( in, taskLazy );

        taskLazy.setName( in.getName() );
        taskLazy.setDescription( in.getDescription() );
        taskLazy.setStart( in.getStart() );
        taskLazy.setEnd( in.getEnd() );
        taskLazy.setClosed( in.getClosed() );

        afterMapping( in, taskLazy );

        return taskLazy;
    }
}
