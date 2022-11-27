package app.spring_webapp_thymeleaf.controllers;

import app.spring_webapp_thymeleaf.entities.AbstractEntity;
import app.spring_webapp_thymeleaf.repositories.MyBaseRepository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Let's extract common code into this base AddController
 *
 * Add template location "pages/module()/add"
 *
 * GET ...url.../entities/add
 *      - shows form for entering a new entity fields
 *
 * POST ...url.../entities/add?id=X&var2=Y&var3=Z
 *      - performs checks for entity valid state before storing
 *      - also must check if for example the entity is not already existing in DB
 */
public abstract class GenericAddFormController<E extends AbstractEntity> {

    protected final MyBaseRepository<E> repository;
    private final GenericController<E> genericController;

    public GenericAddFormController(MyBaseRepository<E> repository,
                                    GenericController<E> genericController) {
        this.repository = repository;
        this.genericController = genericController;
    }

    @ModelAttribute("module")
    String module() {
        return genericController.module();
    }

    @GetMapping
    public String showAddForm(Model model) {
        model.addAttribute("entity", genericController.createNewOne());
        return "pages/" + module() + "/add";
    }

    @PostMapping
    public String addOne(@Valid @ModelAttribute("entity") E entity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "pages/" + module() + "/add";
        }

        if (hasSemanticErrors(entity, bindingResult)) {
            return "pages/" + module() + "/add";
        }

        repository.save(entity);
        return "redirect:/" + module();
    }

    protected boolean hasSemanticErrors(E entity, BindingResult bindingResult) {
        if (doesIdAlreadyExist(entity)) { // trying to add already existing ID
            bindingResult.rejectValue(
                    "id",
                    "form.entity.id.alreadyexists",
                    new Object[]{(entity.getId())},
                    "Id already exists"
            );
            return true;
        }
        return false;
    }

    protected boolean doesIdAlreadyExist(E entity) {
        if (entity != null && entity.getId() != null) {
            return repository.existsById(entity.getId());
        }
        return false;
    }

}