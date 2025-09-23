package de.ruu.app.jeeeraaah.backend.common.mapping.lazy.jpa;

import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA;
import de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA;
import de.ruu.app.jeeeraaah.common.api.domain.TaskLazy;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T19:19:28+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
*/
public class Map_Task_Lazy_JPAImpl implements Map_Task_Lazy_JPA {

    @Override
    public TaskJPA map(TaskGroupJPA group, TaskLazy in) {
        if ( group == null && in == null ) {
            return null;
        }

        TaskJPA taskJPA = create( group, in );

        if ( group != null ) {
            taskJPA.setName( group.getName() );
            taskJPA.description( group.getDescription() );
        }

        return taskJPA;
    }
}
