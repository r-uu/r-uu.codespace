package de.ruu.app.jeeeraaah.common.jpa;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SetAttribute;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.time.LocalDate;

@StaticMetamodel(TaskEntityJPA.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class TaskEntityJPA_ {

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
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#predecessors
	 **/
	public static volatile SetAttribute<TaskEntityJPA, TaskEntityJPA> predecessors;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#subTasks
	 **/
	public static volatile SetAttribute<TaskEntityJPA, TaskEntityJPA> subTasks;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#start
	 **/
	public static volatile SingularAttribute<TaskEntityJPA, LocalDate> start;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#superTask
	 **/
	public static volatile SingularAttribute<TaskEntityJPA, TaskEntityJPA> superTask;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#successors
	 **/
	public static volatile SetAttribute<TaskEntityJPA, TaskEntityJPA> successors;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#description
	 **/
	public static volatile SingularAttribute<TaskEntityJPA, String> description;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#version
	 **/
	public static volatile SingularAttribute<TaskEntityJPA, Short> version;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#taskGroup
	 **/
	public static volatile SingularAttribute<TaskEntityJPA, TaskGroupEntityJPA> taskGroup;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#name
	 **/
	public static volatile SingularAttribute<TaskEntityJPA, String> name;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#closed
	 **/
	public static volatile SingularAttribute<TaskEntityJPA, Boolean> closed;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#end
	 **/
	public static volatile SingularAttribute<TaskEntityJPA, LocalDate> end;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA#id
	 **/
	public static volatile SingularAttribute<TaskEntityJPA, Long> id;
	
	/**
	 * @see de.ruu.app.jeeeraaah.common.jpa.TaskEntityJPA
	 **/
	public static volatile EntityType<TaskEntityJPA> class_;

}

