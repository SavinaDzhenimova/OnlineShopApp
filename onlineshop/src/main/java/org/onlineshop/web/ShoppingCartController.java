package org.onlineshop.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.ShoppingCartDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;
import org.onlineshop.service.interfaces.ProductService;
import org.onlineshop.service.interfaces.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, ProductService productService) {
        this.shoppingCartService = shoppingCartService;
        this.productService = productService;
    }

    @GetMapping("/shopping-cart")
    public ModelAndView showShoppingCart(HttpSession session) {

        ModelAndView modelAndView = new ModelAndView("shopping-cart");

        ShoppingCartDTO shoppingCartDTO = this.shoppingCartService.getCartItemsForCurrentUser(session);

        modelAndView.addObject("shoppingCart", shoppingCartDTO);
        modelAndView.addObject("totalPrice", shoppingCartDTO.getTotalPrice());

        return modelAndView;
    }

    @PostMapping("/products/add-to-shopping-cart/{id}")
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

        Result result = this.productService.addProductToShoppingCart(productId, addCartItemDTO, session);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/shopping-cart");
    }
}
