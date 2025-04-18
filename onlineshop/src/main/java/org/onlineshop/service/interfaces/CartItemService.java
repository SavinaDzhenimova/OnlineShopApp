package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.CartItem;

import java.util.Optional;

public interface CartItemService {

    void saveAndFlush(CartItem cartItem);

    Optional<CartItem> getById(Long id);

    void deleteById(Long id);
}
