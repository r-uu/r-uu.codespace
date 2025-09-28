package de.ruu.lib.jpa.se.hibernate.postgres.demo;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(SimpleTypeEntity.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class SimpleTypeEntity_ extends de.ruu.lib.jpa.core.AbstractEntity_ {

	public static final String NAME = "name";

	
	/**
	 * @see de.ruu.lib.jpa.se.hibernate.postgres.demo.SimpleTypeEntity#name
	 **/
	public static volatile SingularAttribute<SimpleTypeEntity, String> name;
	
	/**
	 * @see de.ruu.lib.jpa.se.hibernate.postgres.demo.SimpleTypeEntity
	 **/
	public static volatile EntityType<SimpleTypeEntity> class_;

}

