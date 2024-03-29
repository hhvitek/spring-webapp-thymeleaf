package app.spring_webapp_thymeleaf.controllers.person;

import app.spring_webapp_thymeleaf.controllers.GenericAddFormController;
import app.spring_webapp_thymeleaf.entities.PersonEntity;
import app.spring_webapp_thymeleaf.repositories.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people/add")
public class PersonAddFormController extends GenericAddFormController<PersonEntity> {


    public PersonAddFormController(PersonRepository repository, PersonController personController) {
        super(repository, personController);
    }
}