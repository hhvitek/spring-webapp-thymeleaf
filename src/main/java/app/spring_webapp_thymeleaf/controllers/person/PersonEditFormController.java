package app.spring_webapp_thymeleaf.controllers.person;

import app.spring_webapp_thymeleaf.controllers.GenericEditFormController;
import app.spring_webapp_thymeleaf.entities.PersonEntity;
import app.spring_webapp_thymeleaf.repositories.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people/edit")
public class PersonEditFormController extends GenericEditFormController<PersonEntity> {

    public PersonEditFormController(PersonRepository repository, PersonController genericController) {
        super(repository, genericController);
    }
}