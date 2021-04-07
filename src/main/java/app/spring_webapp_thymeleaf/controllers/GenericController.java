package app.spring_webapp_thymeleaf.controllers;

import app.spring_webapp_thymeleaf.entities.AbstractEntity;
import app.spring_webapp_thymeleaf.repositories.MyBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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
public abstract class GenericController<R extends MyBaseRepository, E extends AbstractEntity> implements MessageSourceAware {

    private final static int DEFAULT_PAGE_SIZE = 5;

    protected final R repository;
    private final String pageName;
    private MessageSource messageSource;

    public GenericController(R repository, String pageName) {
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

    /**
     * @RequestParam(value = "page") Optional<Integer> page
     * same as
     * @RequestParam(value = "page", required=true) Integer page
     */
    @GetMapping
    public String showAll(
            @RequestParam(value = "page") Optional<Integer> page,
            @RequestParam(value = "size") Optional<Integer> size,
            Model model
    ) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);

        Page<E> entityPage = repository.findAll(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("entityPage", entityPage);

        int totalPages = entityPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

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