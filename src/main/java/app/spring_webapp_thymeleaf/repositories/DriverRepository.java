package app.spring_webapp_thymeleaf.repositories;

import app.spring_webapp_thymeleaf.entities.DriverEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DriverRepository extends MyBaseRepository<DriverEntity> {

    @Query("SELECT d FROM driver d, person p WHERE d.personEntity.id=p.id AND p.canDrive=false")
    List<DriverEntity> findAllDriversWhoCannotDrive();
}