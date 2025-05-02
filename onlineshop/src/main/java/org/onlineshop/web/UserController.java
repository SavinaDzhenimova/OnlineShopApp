package org.onlineshop.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.OrderDTO;
import org.onlineshop.model.exportDTO.ProductsListDTO;
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
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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

    @GetMapping("/favourites")
    public ModelAndView showFavourites(HttpSession session) {

        ModelAndView modelAndView = new ModelAndView("favourites");

        ProductsListDTO favouriteProducts = this.userService.getFavouriteProducts(session);

        modelAndView.addObject("favourites", favouriteProducts);

        if (favouriteProducts.getProducts().isEmpty()) {
            modelAndView.addObject("warningMessage",
                    "Все още няма добавени продукти в списъка Ви с любими!");
        }

        return modelAndView;
    }

    @PostMapping("/favourites/add-favourite/{id}")
    public ModelAndView addProductToFavourites(@PathVariable("id") Long id, HttpSession session,
                                               RedirectAttributes redirectAttributes) {

        Result result = this.userService.addProductToFavourites(id, session);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());

            return new ModelAndView("redirect:/products/all");
        }

        return new ModelAndView("redirect:/users/favourites");
    }

    @DeleteMapping("/favourites/remove-favourite/{id}")
    public ModelAndView removeProductFromFavourites(@PathVariable("id") Long id, HttpSession session,
                                                    RedirectAttributes redirectAttributes) {

        Result result = this.userService.removeProductFromFavourites(id, session);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/users/favourites");
    }
}