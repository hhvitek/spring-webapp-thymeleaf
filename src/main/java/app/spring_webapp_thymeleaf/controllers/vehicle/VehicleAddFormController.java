package app.spring_webapp_thymeleaf.controllers.vehicle;

import app.spring_webapp_thymeleaf.controllers.GenericAddFormController;
import app.spring_webapp_thymeleaf.entities.VehicleEntity;
import app.spring_webapp_thymeleaf.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Handles simple Form to add a new Vehicles
 */
@Controller
@RequestMapping("/vehicles/add")
public class VehicleAddFormController extends GenericAddFormController<VehicleRepository, VehicleEntity> {

    @Autowired
    public VehicleAddFormController(VehicleRepository repository, VehicleController vehicleController) {
        super(repository, vehicleController);
    }
}