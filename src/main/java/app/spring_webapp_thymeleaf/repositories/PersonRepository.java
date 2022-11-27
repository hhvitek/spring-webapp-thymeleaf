package app.spring_webapp_thymeleaf.repositories;

import app.spring_webapp_thymeleaf.entities.PersonEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends MyBaseRepository<PersonEntity> {
}