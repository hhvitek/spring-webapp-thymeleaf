package app.spring_webapp_thymeleaf.controllers;

import app.spring_webapp_thymeleaf.entities.AbstractEntity;
import app.spring_webapp_thymeleaf.repositories.MyBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Let's extract common code into this base Controller
 *
 * Add template location "pages/module()"
 *
 * GET ...url.../entities
 *      - shows table of all entities from DB
 *
 * GET|DELETE ...url.../entities/remove?id=99
 *      - must check if entity referenced by id=99 actually exist in database
 */
public abstract class GenericController<E extends AbstractEntity> implements MessageSourceAware {
    protected final MyBaseRepository<E> repository;
    private final String pageName;
    private MessageSource messageSource;

    public GenericController(MyBaseRepository<E> repository, String pageName) {
        this.repository = repository;
        this.pageName = pageName;
    }

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ModelAttribute("module")
    protected String module() {
        return pageName;
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("entities", repository.findAll());
        return "pages/" + pageName;
    }

    @RequestMapping(value = "/remove", method = { RequestMethod.GET, RequestMethod.DELETE })
    public String removeOne(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        if (!repository.existsById(id)) {
            redirectAttributes.addFlashAttribute("error", getErrorIdNotExistFromMessageSource(id));
            return "redirect:/" + pageName;
        }

        repository.deleteById(id);
        return "redirect:/" + pageName;
    }

    private String getErrorIdNotExistFromMessageSource(Integer id) {
        return messageSource.getMessage("form.entity.id.notexist", new Object[]{(id)}, LocaleContextHolder.getLocale());
    }


    public abstract E createNewOne();
}