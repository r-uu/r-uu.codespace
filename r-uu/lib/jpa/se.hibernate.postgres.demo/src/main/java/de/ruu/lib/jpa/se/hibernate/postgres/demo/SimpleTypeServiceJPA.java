package de.ruu.lib.jpa.se.hibernate.postgres.demo;

import de.ruu.lib.jpa.se.Transactional;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Transactional
@Slf4j
class SimpleTypeServiceJPA implements SimpleTypeService
{
	@Inject private SimpleTypeRepository repository;

	@PostConstruct private void postConstruct() { log.debug("injected repository: {}", repository); }

	@Override public SimpleTypeEntity           save  (SimpleTypeEntity entity) { return repository.create(entity); }
	@Override public SimpleTypeEntity           update(SimpleTypeEntity entity) { return repository.create(entity); }
	@Override public Optional<SimpleTypeEntity> find  (Long             id)     { return repository.find  (id);     }
	@Override public void                       delete(Long             id)     {        repository.delete(id);     }
}