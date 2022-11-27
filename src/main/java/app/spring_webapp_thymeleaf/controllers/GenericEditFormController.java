package app.spring_webapp_thymeleaf.controllers;

import app.spring_webapp_thymeleaf.entities.AbstractEntity;
import app.spring_webapp_thymeleaf.repositories.MyBaseRepository;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Let's extract common code into this base EditController
 *
 * Edit template location "pages/module()/edit"
 *
 * GET ...url.../entities/edit?id=99
 *      - checks if entity already exist
 *
 * POST|PUT ...url.../entities/edit?id=X&var2=Y&var3=Z
 *      - performs checks for entity valid state before storing
 */
public abstract class GenericEditFormController<E extends AbstractEntity> {

    protected final MyBaseRepository<E> repository;
    private final GenericController<E> genericController;

    public GenericEditFormController(MyBaseRepository<E> repository, GenericController<E> genericController) {
        this.repository = repository;
        this.genericController = genericController;
    }

    @ModelAttribute("module")
    String module() {
        return genericController.module();
    }


    @GetMapping
    public String showEditForm(@RequestParam("id") Integer id, Model model) {
        if (repository.existsById(id)) {
            model.addAttribute("entity", repository.findById(id).get());
        } else {
            addErrorBindingToModelBecauseIdNotExist(id, model);
        }

        return "pages/"+ module() + "/edit";
    }

    private void addErrorBindingToModelBecauseIdNotExist(Integer id, Model model) {
        E entity = (E) genericController.createNewOne();
        entity.setId(id);

        BindingResult bindingResult = new BeanPropertyBindingResult(entity, "entity");
        rejectValue(id, bindingResult);

        model.addAttribute("entity", entity);
        model.addAttribute("org.springframework.validation.BindingResult.entity", bindingResult);
    }

    private void rejectValue(Integer id, Errors errors) {
        errors.rejectValue(
                "id",
                "form.entity.id.notexist",
                new Object[]{(id)},
                "Id does not exist."
        );
    }

    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT})
    public String editOne(@ModelAttribute("entity") @Valid E entity, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "pages/"+ module() + "/edit";
        }

        if (hasSemanticErrors(entity, bindingResult)) {
            return "pages/"+ module() + "/edit";
        }

        repository.save(entity);
        return "redirect:/" + module();
    }

    protected boolean hasSemanticErrors(E entity, BindingResult bindingResult) {
        if (!doesIdAlreadyExist(entity)) { // trying to add already existing ID
            rejectValue(entity.getId(), bindingResult);
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