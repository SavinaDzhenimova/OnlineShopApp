package org.onlineshop.service;

import org.onlineshop.model.entity.*;
import org.onlineshop.model.importDTO.AddCartItemDTO;
import org.onlineshop.repository.CartItemRepository;
import org.onlineshop.service.interfaces.CartItemService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Optional<CartItem> getById(Long id) {
        return this.cartItemRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        this.cartItemRepository.deleteById(id);
    }

    @Override
    public void saveAndFlush(CartItem cartItem) {
        this.cartItemRepository.saveAndFlush(cartItem);
    }
}
