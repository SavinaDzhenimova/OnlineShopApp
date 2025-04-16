package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.CartItem;

public interface CartItemService {

    void saveAndFlush(CartItem cartItem);
}
