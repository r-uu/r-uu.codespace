package de.ruu.app.jeeeraaah.backend.common.mapping.lazy.jpa;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.common.api.domain.TaskGroupLazy;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-30T14:08:46+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_TaskGroup_Lazy_JPAImpl implements Map_TaskGroup_Lazy_JPA {

    @Override
    public TaskGroupJPA map(TaskGroupLazy in) {
        if ( in == null ) {
            return null;
        }

        TaskGroupJPA taskGroupJPA = create( in );

        beforeMapping( in, taskGroupJPA );

        afterMapping( in, taskGroupJPA );

        return taskGroupJPA;
    }
}
