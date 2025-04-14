package org.onlineshop.web;

import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.User;
import org.onlineshop.service.interfaces.EmailService;
import org.onlineshop.service.interfaces.PasswordResetService;
import org.onlineshop.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class PasswordResetController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;
    private final EmailService emailService;

    public PasswordResetController(UserService userService, PasswordResetService passwordResetService, EmailService emailService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
        this.emailService = emailService;
    }

    @GetMapping("/forgot-password")
    public ModelAndView showForgotPasswordPage() {

        return new ModelAndView("forgot-password");
    }

    @PostMapping("/forgot-password")
    public ModelAndView sendForgotPasswordEmail(@RequestParam("email") String email,
                                                RedirectAttributes redirectAttributes) {

        Result result = this.userService.sendEmailForForgottenPassword(email);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());

            return new ModelAndView("redirect:/users/forgot-password");
        }

        return new ModelAndView("redirect:/users/login");
    }

    @GetMapping("/reset-password")
    public ModelAndView showResetPassword(@RequestParam("token") String token) {

        ModelAndView modelAndView = new ModelAndView("reset-password");

        modelAndView.addObject("token", token);

        return modelAndView;
    }

    @PostMapping("/reset-password")
    public ModelAndView handleResetPassword(@RequestParam("token") String token,
                                            @RequestParam("password") String password,
                                            @RequestParam("confirmPassword") String confirmPassword,
                                            RedirectAttributes redirectAttributes) {

        Result result = this.userService.resetPassword(password, confirmPassword, token);
        this.passwordResetService.markTokenAsUsed(token);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());

            return new ModelAndView("redirect:/users/reset-password?token=" + token);
        }

        return new ModelAndView("redirect:/users/login");
    }
}
