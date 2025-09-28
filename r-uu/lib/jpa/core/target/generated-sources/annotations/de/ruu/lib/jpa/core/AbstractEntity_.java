package de.ruu.lib.jpa.core;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.MappedSuperclassType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(AbstractEntity.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class AbstractEntity_ {

	public static final String ID = "id";
	public static final String VERSION = "version";

	
	/**
	 * @see de.ruu.lib.jpa.core.AbstractEntity#id
	 **/
	public static volatile SingularAttribute<AbstractEntity, Long> id;
	
	/**
	 * @see de.ruu.lib.jpa.core.AbstractEntity
	 **/
	public static volatile MappedSuperclassType<AbstractEntity> class_;
	
	/**
	 * @see de.ruu.lib.jpa.core.AbstractEntity#version
	 **/
	public static volatile SingularAttribute<AbstractEntity, Short> version;

}

