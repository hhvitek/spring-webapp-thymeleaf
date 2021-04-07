package app.spring_webapp_thymeleaf.repositories;

import app.spring_webapp_thymeleaf.entities.VehicleEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends MyBaseRepository<VehicleEntity, Integer> {
}