package org.onlineshop.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.ShoppingCartDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;
import org.onlineshop.service.interfaces.ShoppingCartService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping
    public ModelAndView showShoppingCart(HttpSession session, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("shopping-cart");

        ShoppingCartDTO shoppingCartDTO = this.shoppingCartService.getCartItemsForCurrentUser(session);

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

        Result result = this.shoppingCartService.addProductToShoppingCart(productId, addCartItemDTO, session);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/shopping-cart");
    }

    @PostMapping("/remove-item/{id}")
    public ModelAndView removeProductFromShoppingCart(@PathVariable("id") Long cartItemId,
                                                      RedirectAttributes redirectAttributes, HttpSession session) {

        Result result = this.shoppingCartService.removeItemFromShoppingCart(cartItemId, session);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/shopping-cart");
    }
}