package org.onlineshop.web;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.*;
import org.onlineshop.service.interfaces.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalController {

    private final BrandService brandService;
    private final ColorService colorService;
    private final CategoryService categoryService;
    private final ShoeSizeService shoeSizeService;
    private final ShoppingCartService shoppingCartService;
    private final UserService userService;

    public GlobalController(BrandService brandService, ColorService colorService, CategoryService categoryService,
                            ShoeSizeService shoeSizeService, ShoppingCartService shoppingCartService,
                            UserService userService) {
        this.brandService = brandService;
        this.colorService = colorService;
        this.categoryService = categoryService;
        this.shoeSizeService = shoeSizeService;
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
    }

    @ModelAttribute("brands")
    public List<Brand> getBrands() {
        return this.brandService.getAllBrands();
    }

    @ModelAttribute("colors")
    public List<Color> getColors() {
        return this.colorService.getAllColors();
    }

    @ModelAttribute("categoryIds")
    public List<Category> getCategories() {
        return this.categoryService.getAllCategories();
    }

    @ModelAttribute("shoeSizes")
    public List<ShoeSize> shoeSizes() {
        return this.shoeSizeService.getAllShoeSizes();
    }

    @ModelAttribute
    public void addCartIndicatorToModel(HttpSession session, Model model) {
        ShoppingCart cart = (ShoppingCart) session.getAttribute("guestCart");
        if (cart == null) {
            cart = this.userService.getLoggedUserShoppingCart(session);
        }

        boolean hasItemsInCart = cart != null && !cart.getCartItems().isEmpty();

        model.addAttribute("hasItemsInCart", hasItemsInCart);
    }
}