package app.spring_webapp_thymeleaf.controllers.vehicle;

import app.spring_webapp_thymeleaf.controllers.GenericEditFormController;
import app.spring_webapp_thymeleaf.entities.VehicleEntity;
import app.spring_webapp_thymeleaf.repositories.VehicleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vehicles/edit")
public class VehicleEditFormController extends GenericEditFormController<VehicleEntity> {

    public VehicleEditFormController(VehicleRepository repository, VehicleController vehicleController) {
        super(repository, vehicleController);
    }
}