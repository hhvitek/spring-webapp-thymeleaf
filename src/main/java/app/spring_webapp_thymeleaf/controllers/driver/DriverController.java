package app.spring_webapp_thymeleaf.controllers.driver;

import app.spring_webapp_thymeleaf.controllers.GenericController;
import app.spring_webapp_thymeleaf.entities.DriverEntity;
import app.spring_webapp_thymeleaf.repositories.DriverRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/drivers")
public class DriverController extends GenericController<DriverEntity> {

    private static final String PAGE_NAME = "drivers";
    private final DriverRepository repository;

    public DriverController(DriverRepository repository) {
        super(repository, PAGE_NAME);
        this.repository = repository;
    }

    @Override
    public DriverEntity createNewOne() {
        return new DriverEntity();
    }

    @GetMapping("/cannot")
    public String showDriversWhoCannotDrive(Model model) {
        model.addAttribute("entities", repository.findAllDriversWhoCannotDrive());
        return "pages/" + module();
    }
}