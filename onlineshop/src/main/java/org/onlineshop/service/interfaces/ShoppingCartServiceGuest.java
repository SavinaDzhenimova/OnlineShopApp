package org.onlineshop.service.interfaces;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.Product;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.ShoppingCart;
import org.onlineshop.model.exportDTO.ShoppingCartDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;

public interface ShoppingCartServiceGuest {

    Result addProductToShoppingCart(Long productId, AddCartItemDTO dto, HttpSession session);

    ShoppingCartDTO getCartItemsForCurrentUser(HttpSession session);

    Result addItemToCart(Product product, AddCartItemDTO addCartItemDTO, ShoppingCart cart);

    Result removeItemFromShoppingCart(Long cartItemId, HttpSession session);
}
