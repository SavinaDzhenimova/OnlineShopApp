package org.onlineshop.service;

import org.onlineshop.model.entity.ShoppingCart;
import org.onlineshop.repository.ShoppingCartRepository;
import org.onlineshop.service.interfaces.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public void saveAndFlush(ShoppingCart shoppingCart) {
        this.shoppingCartRepository.saveAndFlush(shoppingCart);
    }
}