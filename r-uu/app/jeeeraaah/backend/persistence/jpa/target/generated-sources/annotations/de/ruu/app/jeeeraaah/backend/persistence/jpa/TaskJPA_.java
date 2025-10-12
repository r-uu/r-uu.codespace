package de.ruu.app.jeeeraaah.backend.persistence.jpa;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@StaticMetamodel(TaskJPA.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class TaskJPA_ {

	public static final String PREDECESSORS = "predecessors";
	public static final String SUB_TASKS = "subTasks";
	public static final String START = "start";
	public static final String SUPER_TASK = "superTask";
	public static final String SUCCESSORS = "successors";
	public static final String DESCRIPTION = "description";
	public static final String VERSION = "version";
	public static final String TASK_GROUP = "taskGroup";
	public static final String NAME = "name";
	public static final String CLOSED = "closed";
	public static final String END = "end";
	public static final String ID = "id";

	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#predecessors
	 **/
	public static volatile SetAttribute<TaskJPA, TaskJPA> predecessors;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#subTasks
	 **/
	public static volatile SetAttribute<TaskJPA, TaskJPA> subTasks;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#start
	 **/
	public static volatile SingularAttribute<TaskJPA, LocalDate> start;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#superTask
	 **/
	public static volatile SingularAttribute<TaskJPA, TaskJPA> superTask;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#successors
	 **/
	public static volatile SetAttribute<TaskJPA, TaskJPA> successors;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#description
	 **/
	public static volatile SingularAttribute<TaskJPA, String> description;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#version
	 **/
	public static volatile SingularAttribute<TaskJPA, Short> version;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#taskGroup
	 **/
	public static volatile SingularAttribute<TaskJPA, TaskGroupJPA> taskGroup;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#name
	 **/
	public static volatile SingularAttribute<TaskJPA, String> name;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#closed
	 **/
	public static volatile SingularAttribute<TaskJPA, Boolean> closed;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#end
	 **/
	public static volatile SingularAttribute<TaskJPA, LocalDate> end;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA#id
	 **/
	public static volatile SingularAttribute<TaskJPA, Long> id;
	
	/**
	 * @see de.ruu.app.jeeeraaah.backend.persistence.jpa.TaskJPA
	 **/
	public static volatile EntityType<TaskJPA> class_;

}

