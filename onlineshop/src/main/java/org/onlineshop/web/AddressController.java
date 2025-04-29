package org.onlineshop.web;

import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.AddressDTO;
import org.onlineshop.model.importDTO.AddAddressDTO;
import org.onlineshop.model.user.UserRegisterDTO;
import org.onlineshop.service.interfaces.AddressService;
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
public class AddressController {

    private final UserService userService;
    private final AddressService addressService;

    public AddressController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @GetMapping("/my-addresses")
    public ModelAndView showMyAddresses() {
        ModelAndView modelAndView = new ModelAndView("addresses");

        List<AddressDTO> loggedUserAddresses = this.userService.getLoggedUserAddresses();

        if (loggedUserAddresses.isEmpty()) {
            modelAndView.addObject("warningMessage", "Все още нямате добавени адреси.");
        } else {
            modelAndView.addObject("addresses", loggedUserAddresses);
        }

        return modelAndView;
    }

    @GetMapping("/add-address")
    public ModelAndView showAddAddress(Model model) {

        if (!model.containsAttribute("addAddressDTO")) {
            model.addAttribute("addAddressDTO", new AddAddressDTO());
        }

        return new ModelAndView("add-address");
    }

    @PostMapping("/add-address")
    public ModelAndView addAddress(@Valid @ModelAttribute("addAddressDTO") AddAddressDTO addAddressDTO,
                                   BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addAddressDTO", addAddressDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addAddressDTO",
                            bindingResult);

            return new ModelAndView("redirect:/users/add-address");
        }

        Result result = this.userService.addAddress(addAddressDTO);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/users/my-addresses");
    }

    @DeleteMapping("/delete-address/{id}")
    public ModelAndView deleteAddress(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        Result result = this.userService.deleteAddress(id);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/users/my-addresses");
    }
}
