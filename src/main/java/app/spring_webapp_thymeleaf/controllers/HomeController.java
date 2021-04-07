package app.spring_webapp_thymeleaf.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Starting point of this application "/" endpoint
 */
@Controller
public class HomeController {

    @ModelAttribute("module")
    String module() {
        return "home";
    }

    @GetMapping(value = {"/api/home", "/", "/welcome"})
    public String showIndex(Model model) {
        return "pages/home";
    }
}