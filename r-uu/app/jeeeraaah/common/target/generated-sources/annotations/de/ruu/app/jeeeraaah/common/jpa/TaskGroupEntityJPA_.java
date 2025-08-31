package de.ruu.app.jeeeraaah.common.jpa;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TaskGroupEntityJPA.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class TaskGroupEntityJPA_ {

	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA#name
	 **/
	public static volatile SingularAttribute<TaskGroupEntityJPA, String> name;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA#description
	 **/
	public static volatile SingularAttribute<TaskGroupEntityJPA, String> description;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA#id
	 **/
	public static volatile SingularAttribute<TaskGroupEntityJPA, Long> id;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA
	 **/
	public static volatile EntityType<TaskGroupEntityJPA> class_;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA#version
	 **/
	public static volatile SingularAttribute<TaskGroupEntityJPA, Short> version;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskGroupEntityJPA#tasks
	 **/
	public static volatile SetAttribute<TaskGroupEntityJPA, TaskEntityJPA> tasks;

	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String VERSION = "version";
	public static final String TASKS = "tasks";

}

