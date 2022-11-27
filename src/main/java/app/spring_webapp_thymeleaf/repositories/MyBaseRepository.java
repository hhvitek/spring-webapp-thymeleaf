package app.spring_webapp_thymeleaf.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import app.spring_webapp_thymeleaf.entities.AbstractEntity;

/**
 * Common repository base
 */
@NoRepositoryBean
public interface MyBaseRepository<T extends AbstractEntity> extends CrudRepository<T, Integer> {
}