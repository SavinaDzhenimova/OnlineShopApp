package org.onlineshop.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.exportDTO.ShoppingCartDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;
import org.onlineshop.service.interfaces.ShoppingCartServiceGuest;
import org.onlineshop.service.interfaces.ShoppingCartServiceLogged;
import org.onlineshop.service.utils.CurrentUserProvider;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartServiceLogged shoppingCartServiceLogged;
    private final ShoppingCartServiceGuest shoppingCartServiceGuest;
    private final CurrentUserProvider currentUserProvider;

    public ShoppingCartController(ShoppingCartServiceLogged shoppingCartServiceLogged,
                                  ShoppingCartServiceGuest shoppingCartServiceGuest,
                                  CurrentUserProvider currentUserProvider) {
        this.shoppingCartServiceLogged = shoppingCartServiceLogged;
        this.shoppingCartServiceGuest = shoppingCartServiceGuest;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    public ModelAndView showShoppingCart(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("shopping-cart");

        ShoppingCartDTO shoppingCartDTO;

        User loggedUser = this.currentUserProvider.getLoggedUser();

        if (loggedUser != null) {
            shoppingCartDTO = this.shoppingCartServiceLogged.getCartItemsForCurrentUser(loggedUser, session);
        } else {
            shoppingCartDTO = this.shoppingCartServiceGuest.getCartItemsForCurrentUser(session);
        }

        modelAndView.addObject("shoppingCart", shoppingCartDTO);
        modelAndView.addObject("totalPrice", shoppingCartDTO.getTotalPrice());

        return modelAndView;
    }

    @PostMapping("/add-item/{id}")
    public ModelAndView addProductToShoppingCart(@PathVariable("id") Long productId,
                                                 @Valid @ModelAttribute("addCartItemDTO") AddCartItemDTO addCartItemDTO,
                                                 BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                                 HttpSession session) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addCartItemDTO", addCartItemDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addCartItemDTO",
                            bindingResult);

            return new ModelAndView("redirect:/products/product/" + productId);
        }

        Result result;

        User loggedUser = this.currentUserProvider.getLoggedUser();

        if (loggedUser != null) {
            result = this.shoppingCartServiceLogged.addProductToShoppingCart(loggedUser, productId, addCartItemDTO, session);
        } else {
            result = this.shoppingCartServiceGuest.addProductToShoppingCart(productId, addCartItemDTO, session);
        }

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/shopping-cart");
    }

    @DeleteMapping("/remove-item/{id}")
    public ModelAndView removeProductFromShoppingCart(@PathVariable("id") String cartItemIdStr,
                                                      RedirectAttributes redirectAttributes, HttpSession session) {

        Long cartItemId;
        try {
            cartItemId = Long.valueOf(cartItemIdStr);
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("failureMessage", "Невалиден идентификатор на артикул.");
            return new ModelAndView("redirect:/shopping-cart");
        }

        Result result;

        User loggedUser = this.currentUserProvider.getLoggedUser();

        if (loggedUser != null) {
            result = this.shoppingCartServiceLogged.removeItemFromShoppingCart(loggedUser, cartItemId);
        } else {
            result = this.shoppingCartServiceGuest.removeItemFromShoppingCart(cartItemId, session);
        }

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/shopping-cart");
    }
}