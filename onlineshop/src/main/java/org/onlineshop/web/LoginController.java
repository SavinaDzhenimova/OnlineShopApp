package org.onlineshop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class LoginController {

    @GetMapping("/login")
    public ModelAndView login() {

        return new ModelAndView("login-form");
    }

    @GetMapping("/login-error")
    public ModelAndView loginError(RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("failureMessage",
                "Невалидно потребителско име или парола!");

        return new ModelAndView("redirect:/users/login");
    }
}