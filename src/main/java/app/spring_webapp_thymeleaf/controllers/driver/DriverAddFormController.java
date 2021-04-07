package app.spring_webapp_thymeleaf.controllers.driver;


import app.spring_webapp_thymeleaf.controllers.GenericAddFormController;
import app.spring_webapp_thymeleaf.entities.DriverEntity;
import app.spring_webapp_thymeleaf.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/drivers/add")
public class DriverAddFormController extends GenericAddFormController<DriverRepository, DriverEntity> {

    private final DriverValidator driverValidator;

    @Autowired
    public DriverAddFormController(DriverRepository repository, DriverController driverController, DriverValidator driverValidator) {
        super(repository, driverController);
        this.driverValidator = driverValidator;
    }

    @Override
    protected boolean hasSemanticErrors(DriverEntity entity, BindingResult bindingResult) {
        super.hasSemanticErrors(entity, bindingResult);

        if (entity.getId() != null && repository.existsById(entity.getId())) {
            bindingResult.rejectValue(
                    "id",
                    "form.entity.id.alreadyexists",
                    new Object[]{(entity.getId())},
                    "Id already exists"
            );
        }

        driverValidator.validate(entity, bindingResult);
        return bindingResult.hasErrors();
    }
}