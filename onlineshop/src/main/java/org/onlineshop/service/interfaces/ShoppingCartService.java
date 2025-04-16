package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.ShoppingCart;

public interface ShoppingCartService {

    void saveAndFlush(ShoppingCart shoppingCart);
}
