package org.onlineshop.web;

import org.onlineshop.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

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

    @GetMapping("/login/forgot-password")
    public ModelAndView showForgotPasswordPage() {

        return new ModelAndView("forgot-password");
    }

    @PostMapping("/login/forgot-password")
    public ModelAndView sendForgotPasswordEmail(@RequestParam("emailOrPhoneNumber") String emailOrPhoneNumber,
                                                RedirectAttributes redirectAttributes) {

        return new ModelAndView("redirect:/users/login");
    }
}