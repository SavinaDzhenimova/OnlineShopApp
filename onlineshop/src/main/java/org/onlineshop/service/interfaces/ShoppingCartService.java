package org.onlineshop.service.interfaces;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.Product;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.ShoppingCart;
import org.onlineshop.model.importDTO.AddCartItemDTO;

public interface ShoppingCartService {

    Result addItemToCart(Product product, AddCartItemDTO addCartItemDTO, ShoppingCart cart);

    void saveAndFlush(ShoppingCart shoppingCart);
}
