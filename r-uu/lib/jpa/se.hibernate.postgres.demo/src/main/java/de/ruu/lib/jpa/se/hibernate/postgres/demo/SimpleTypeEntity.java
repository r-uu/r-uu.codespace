package de.ruu.lib.jpa.se.hibernate.postgres.demo;

import de.ruu.lib.jpa.core.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.Accessors;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(schema = "lib_test", name = "simple_type")
@Getter
@Accessors(fluent = true) // generate fluent style getters but also implement java bean style getters
                          // to comply to java bean conventions
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = PROTECTED) // required by jpa
class SimpleTypeEntity extends AbstractEntity<SimpleTypeDTO> implements SimpleType
{
	@Setter private String name;

	SimpleTypeEntity(String name) { this.name  = name; }

	public SimpleTypeDTO toDTO() { return new SimpleTypeDTO(name); }
}