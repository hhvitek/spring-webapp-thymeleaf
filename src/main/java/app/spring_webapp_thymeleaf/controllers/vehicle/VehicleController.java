package app.spring_webapp_thymeleaf.controllers.vehicle;

import app.spring_webapp_thymeleaf.controllers.GenericController;
import app.spring_webapp_thymeleaf.entities.VehicleEntity;
import app.spring_webapp_thymeleaf.repositories.VehicleRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vehicles")
public class VehicleController extends GenericController<VehicleEntity> {

    private static final String PAGE_NAME = "vehicles";

    public VehicleController(VehicleRepository repository) {
        super(repository, PAGE_NAME);
    }

    @Override
    public VehicleEntity createNewOne() {
        return new VehicleEntity();
    }
}