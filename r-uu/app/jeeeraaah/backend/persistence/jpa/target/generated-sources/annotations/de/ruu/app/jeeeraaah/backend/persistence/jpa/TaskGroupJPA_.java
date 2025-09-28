package de.ruu.app.jeeeraaah.backend.persistence.jpa;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TaskGroupJPA.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class TaskGroupJPA_ {

	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String VERSION = "version";
	public static final String TASKS = "tasks";

	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA#name
	 **/
	public static volatile SingularAttribute<TaskGroupJPA, String> name;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA#description
	 **/
	public static volatile SingularAttribute<TaskGroupJPA, String> description;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA#id
	 **/
	public static volatile SingularAttribute<TaskGroupJPA, Long> id;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA
	 **/
	public static volatile EntityType<TaskGroupJPA> class_;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA#version
	 **/
	public static volatile SingularAttribute<TaskGroupJPA, Short> version;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskGroupJPA#tasks
	 **/
	public static volatile SetAttribute<TaskGroupJPA, TaskJPA> tasks;

}

