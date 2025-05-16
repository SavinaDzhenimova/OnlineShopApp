package org.onlineshop.web;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.*;
import org.onlineshop.model.enums.Region;
import org.onlineshop.service.interfaces.*;
import org.onlineshop.service.utils.CurrentUserProvider;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class GlobalController {

    private final BrandService brandService;
    private final ColorService colorService;
    private final CategoryService categoryService;
    private final ShoeSizeService shoeSizeService;
    private final ProductService productService;
    private final CurrentUserProvider currentUserProvider;

    public GlobalController(BrandService brandService, ColorService colorService, CategoryService categoryService,
                            ShoeSizeService shoeSizeService, ProductService productService,
                            CurrentUserProvider currentUserProvider) {
        this.brandService = brandService;
        this.colorService = colorService;
        this.categoryService = categoryService;
        this.shoeSizeService = shoeSizeService;
        this.productService = productService;
        this.currentUserProvider = currentUserProvider;
    }

    @ModelAttribute("brands")
    public List<Brand> getBrands() {
        return this.brandService.getAllBrands();
    }

    @ModelAttribute("colors")
    public List<Color> getColors() {
        return this.colorService.getAllColors();
    }

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return this.categoryService.getAllCategories();
    }

    @ModelAttribute("shoeSizes")
    public List<ShoeSize> shoeSizes() {
        return this.shoeSizeService.getAllShoeSizes();
    }

    @ModelAttribute("regions")
    public List<Region> regions() {
        return List.of(Region.values());
    }

    @ModelAttribute
    public void addCartIndicatorToModel(HttpSession session, Model model) {
        ShoppingCart shoppingCart;

        User loggedUser = this.currentUserProvider.getLoggedUser();

        if (loggedUser != null) {
            shoppingCart = loggedUser.getShoppingCart();
        } else {
            shoppingCart = (ShoppingCart) session.getAttribute("guestCart");
        }

        boolean hasItemsInCart = shoppingCart != null && shoppingCart.getCartItems() != null &&
                !shoppingCart.getCartItems().isEmpty();

        int cartItemsCount = shoppingCart != null ? shoppingCart.getCartItems().stream()
                .mapToInt(CartItem::getQuantity).sum(): 0;

        model.addAttribute("hasItemsInCart", hasItemsInCart);
        model.addAttribute("cartItemsCount", cartItemsCount);
    }

    @ModelAttribute
    public void addFavouritesIndicatorToModel(HttpSession session, Model model) {
        List<Product> favourites;

        User loggedUser = this.currentUserProvider.getLoggedUser();

        if (loggedUser != null) {
            favourites = loggedUser.getFavourites().stream().toList();
        } else {
            List<Long> favouriteIds = (List<Long>) session.getAttribute("guestFavourites");

            if (favouriteIds != null) {
                favourites = favouriteIds.stream()
                        .map(id -> this.productService.getById(id).orElse(null))
                        .filter(Objects::nonNull)
                        .toList();
            } else {
                favourites = Collections.emptyList();
            }
        }

        boolean hasItemsInFavourites = !favourites.isEmpty();

        int favouriteItemsCount = favourites.size();

        model.addAttribute("hasItemsInFavourites", hasItemsInFavourites);
        model.addAttribute("favouriteItemsCount", favouriteItemsCount);
    }
}