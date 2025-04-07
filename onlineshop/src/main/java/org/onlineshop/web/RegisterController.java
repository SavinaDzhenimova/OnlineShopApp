package org.onlineshop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class RegisterController {

    @GetMapping("/register")
    public ModelAndView register() {

        return new ModelAndView("register-form");
    }
}
