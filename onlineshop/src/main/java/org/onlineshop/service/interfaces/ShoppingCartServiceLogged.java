package org.onlineshop.service.interfaces;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.Product;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.ShoppingCart;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.exportDTO.ShoppingCartDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;

public interface ShoppingCartServiceLogged {

    Result addProductToShoppingCart(User loggedUser, Long productId, AddCartItemDTO dto, HttpSession session);

    ShoppingCartDTO getCartItemsForCurrentUser(User loggedUser, HttpSession session);

    Result addItemToCart(Product product, AddCartItemDTO addCartItemDTO, ShoppingCart cart);

    Result removeItemFromShoppingCart(User loggedUser, Long cartItemId, HttpSession session);

    void saveAndFlush(ShoppingCart shoppingCart);
}
