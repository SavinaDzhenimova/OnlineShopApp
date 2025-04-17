package org.onlineshop.service;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.*;
import org.onlineshop.model.importDTO.AddCartItemDTO;
import org.onlineshop.repository.ShoppingCartRepository;
import org.onlineshop.service.interfaces.CartItemService;
import org.onlineshop.service.interfaces.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemService cartItemService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, CartItemService cartItemService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemService = cartItemService;
    }

    @Override
    public Result addItemToCart(Product product, AddCartItemDTO addCartItemDTO, ShoppingCart cart) {
        Optional<QuantitySize> matchingSize = product.getQuantitySize()
                .stream()
                .filter(quantitySize -> quantitySize.getSize().getSize().equals(addCartItemDTO.getSize()))
                .findFirst();

        if (matchingSize.isEmpty()) {
            return new Result(false, "Избраният размер не е наличен за този продукт!");
        }

        int availableQuantity = matchingSize.get().getQuantity();
        int requestedQuantity = addCartItemDTO.getQuantity();

        if (availableQuantity < requestedQuantity) {
            return new Result(false, "Наличното количество за размер " +
                    addCartItemDTO.getSize() + " е само " + availableQuantity + " броя.");
        }

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(requestedQuantity);
        cartItem.setSize(addCartItemDTO.getSize());

        if (cart.getId() != null) {
            this.cartItemService.saveAndFlush(cartItem);
        } else {
            cart.getCartItems().add(cartItem);
        }

        return new Result(true, "Успешно добавихте този продукт във вашата количка!");
    }

    @Override
    public void saveAndFlush(ShoppingCart shoppingCart) {
        this.shoppingCartRepository.saveAndFlush(shoppingCart);
    }
}