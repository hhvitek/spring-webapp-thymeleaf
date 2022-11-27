package app.spring_webapp_thymeleaf.controllers.person;

import app.spring_webapp_thymeleaf.controllers.GenericController;
import app.spring_webapp_thymeleaf.entities.PersonEntity;
import app.spring_webapp_thymeleaf.repositories.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people")
public class PersonController extends GenericController<PersonEntity> {

    private final static String PAGE_NAME = "people";

    public PersonController(PersonRepository repository) {
        super(repository, PAGE_NAME);
    }

    @Override
    public PersonEntity createNewOne() {
        return new PersonEntity();
    }
}