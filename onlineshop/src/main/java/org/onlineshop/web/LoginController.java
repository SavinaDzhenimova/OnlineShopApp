package org.onlineshop.web;

import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.AddressDTO;
import org.onlineshop.model.exportDTO.OrderDTO;
import org.onlineshop.model.exportDTO.VipStatusDTO;
import org.onlineshop.model.user.UserDTO;
import org.onlineshop.service.interfaces.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

    @GetMapping("/profile")
    public ModelAndView profile(Model model) {

        if (!model.containsAttribute("userDTO")) {
            model.addAttribute("userDTO", new UserDTO());
        }

        ModelAndView modelAndView = new ModelAndView("profile");

        UserDTO userDTO = this.userService.getLoggedUserInfoForProfilePage();
        VipStatusDTO vipStatusDTO = this.userService.calculateVipStatus();

        modelAndView.addObject("userDTO", userDTO);
        modelAndView.addObject("vipStatus", vipStatusDTO);

        return modelAndView;
    }

    @PutMapping("/update-profile")
    public ModelAndView updateProfileInfo(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                                          BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userDTO", userDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userDTO",
                            bindingResult);

            return new ModelAndView("profile");
        }

        this.userService.updateUserInfo(userDTO);
        this.userService.refreshAuthentication(userDTO.getEmail());

        return new ModelAndView("redirect:/users/profile");
    }

    @GetMapping("/my-orders")
    public ModelAndView showMyOrders() {
        List<OrderDTO> loggedUserOrders = this.userService.getLoggedUserOrders();

        ModelAndView modelAndView = new ModelAndView("orders");

        if (loggedUserOrders.isEmpty()) {
            modelAndView.addObject("warningMessage", "Все още нямате направени поръчки.");
        } else {
            modelAndView.addObject("orders", loggedUserOrders);
        }

        return modelAndView;
    }
}