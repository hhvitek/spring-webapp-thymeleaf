package app.spring_webapp_thymeleaf.controllers.driver;

import app.spring_webapp_thymeleaf.controllers.GenericEditFormController;
import app.spring_webapp_thymeleaf.entities.DriverEntity;
import app.spring_webapp_thymeleaf.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/drivers/edit")
public class DriverEditFormController extends GenericEditFormController<DriverRepository, DriverEntity> {

    private final DriverValidator validator;

    @Autowired
    public DriverEditFormController(DriverRepository repository, DriverController driverController, DriverValidator validator) {
        super(repository, driverController);
        this.validator = validator;
    }

    @Override
    protected boolean hasSemanticErrors(DriverEntity entity, BindingResult bindingResult) {
        super.hasSemanticErrors(entity, bindingResult);

        if (entity.getId() == null || !repository.existsById(entity.getId())) {
            bindingResult.rejectValue(
                    "id",
                    "form.entity.id.notexist",
                    new Object[]{(entity.getId())},
                    "Id does not exist."
            );
        }

        validator.validate(entity, bindingResult);
        return bindingResult.hasErrors();
    }
}