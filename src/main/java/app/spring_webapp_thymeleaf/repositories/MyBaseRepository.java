package app.spring_webapp_thymeleaf.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Common repository base
 */
@NoRepositoryBean
public interface MyBaseRepository<T extends Serializable, ID extends Serializable> extends CrudRepository<T, ID> {
    List<T> findAll();
}