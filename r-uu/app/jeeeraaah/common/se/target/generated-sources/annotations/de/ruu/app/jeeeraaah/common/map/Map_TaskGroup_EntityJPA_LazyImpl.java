package de.ruu.app.jeeeraaah.common.map;

import de.ruu.app.jeeeraaah.common.dto.TaskGroupLazy;
import de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-07T07:13:09+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
public class Map_TaskGroup_EntityJPA_LazyImpl implements Map_TaskGroup_EntityJPA_Lazy {

    @Override
    public TaskGroupLazy map(TaskGroupEntityJPA in) {
        if ( in == null ) {
            return null;
        }

        TaskGroupLazy taskGroupLazy = new TaskGroupLazy();

        beforeMapping( in, taskGroupLazy );

        taskGroupLazy.setName( in.getName() );

        afterMapping( in, taskGroupLazy );

        return taskGroupLazy;
    }
}
