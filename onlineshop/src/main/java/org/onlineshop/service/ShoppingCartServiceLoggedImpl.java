package org.onlineshop.service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.onlineshop.model.entity.*;
import org.onlineshop.model.exportDTO.CartItemDTO;
import org.onlineshop.model.exportDTO.ShoppingCartDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;
import org.onlineshop.model.importDTO.QuantitySizeDTO;
import org.onlineshop.repository.ShoppingCartRepository;
import org.onlineshop.service.interfaces.CartItemService;
import org.onlineshop.service.interfaces.ProductService;
import org.onlineshop.service.interfaces.ShoppingCartServiceLogged;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceLoggedImpl implements ShoppingCartServiceLogged {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemService cartItemService;
    private final ProductService productService;

    public ShoppingCartServiceLoggedImpl(ShoppingCartRepository shoppingCartRepository, CartItemService cartItemService,
                                         ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    @Transactional
    @Override
    public ShoppingCartDTO getCartItemsForCurrentUser(User loggedUser, HttpSession session) {
        if (loggedUser == null) {
            return new ShoppingCartDTO(new ArrayList<>(), BigDecimal.ZERO);
        }

        ShoppingCart shoppingCart = loggedUser.getShoppingCart();
        if (shoppingCart == null || shoppingCart.getCartItems() == null) {
            return new ShoppingCartDTO(new ArrayList<>(), BigDecimal.ZERO);
        }

        List<CartItemDTO> cartItemDTOList = shoppingCart.getCartItems().stream()
                .map(this::mapToCartItemDTO)
                .collect(Collectors.toList());

        BigDecimal totalPrice = shoppingCart.getCartItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ShoppingCartDTO(cartItemDTOList, totalPrice);
    }

    private CartItemDTO mapToCartItemDTO(CartItem cartItem) {
        CartItemDTO cartItemDTO = new CartItemDTO();
        Product product = cartItem.getProduct();

        cartItemDTO.setId(cartItem.getId());
        if (cartItem.getId() == null && cartItem.getTempId() != null) {
            cartItemDTO.setTempId(cartItem.getTempId());
        }

        cartItemDTO.setName(product.getName());
        cartItemDTO.setImageUrl(product.getImages().get(0).getImageUrl());
        cartItemDTO.setCategory(product.getCategory().getCategoryName().getDisplayName());
        cartItemDTO.setSelectedQuantity(cartItem.getQuantity());
        cartItemDTO.setSelectedSize(cartItem.getSize());
        cartItemDTO.setUnitPrice(product.getPrice());
        cartItemDTO.setQuantityPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        cartItemDTO.setProductId(product.getId());

        Set<QuantitySizeDTO> quantitySizes = new TreeSet<>(product.getQuantitySize().stream()
                .filter(quantitySize -> quantitySize.getQuantity() > 0)
                .map(quantitySize -> {
                    QuantitySizeDTO quantitySizeDTO = new QuantitySizeDTO();
                    quantitySizeDTO.setSize(quantitySize.getSize().getSize());
                    quantitySizeDTO.setQuantity(quantitySize.getQuantity());
                    return quantitySizeDTO;
                }).toList());

        cartItemDTO.setQuantitySizes(quantitySizes);

        return cartItemDTO;
    }

    @Override
    public Result addProductToShoppingCart(User loggedUser, Long productId, AddCartItemDTO addCartItemDTO, HttpSession session) {
        addCartItemDTO.setProductId(productId);
        Optional<Product> optionalProduct = this.productService.getById(addCartItemDTO.getProductId());

        if (optionalProduct.isEmpty()) {
            return new Result(false, "Продуктът, който се опитвате да добавите в количката, не съществува!");
        }

        Product product = optionalProduct.get();
        ShoppingCart shoppingCart = loggedUser.getShoppingCart();

        return this.addItemToCart(product, addCartItemDTO, shoppingCart);
    }

    @Override
    public Result addItemToCart(Product product, AddCartItemDTO addCartItemDTO, ShoppingCart shoppingCart) {
        Optional<QuantitySize> matchingSize = product.getQuantitySize()
                .stream()
                .filter(quantitySize -> quantitySize.getSize().getSize().equals(addCartItemDTO.getSize()))
                .findFirst();

        if (matchingSize.isEmpty()) {
            return new Result(false, "Избраният размер не е наличен за този продукт!");
        }

        int requestedQuantity = addCartItemDTO.getQuantity();

        Optional<CartItem> existingCartItemOpt = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId())
                        && cartItem.getSize() == (addCartItemDTO.getSize()))
                .findFirst();

        if (existingCartItemOpt.isPresent()) {
            CartItem existingCartItem = existingCartItemOpt.get();
            int newQuantity = existingCartItem.getQuantity() + addCartItemDTO.getQuantity();

            if (newQuantity > matchingSize.get().getQuantity()) {
                return new Result(false, "Наличността за размер " + addCartItemDTO.getSize() +
                        " е само " + matchingSize.get().getQuantity() + " броя.");
            }

            existingCartItem.setQuantity(newQuantity);
            cartItemService.saveAndFlush(existingCartItem);
            return new Result(true, "Актуализирахме количеството на този продукт във вашата количка!");
        }

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setProduct(product);
        cartItem.setQuantity(requestedQuantity);
        cartItem.setSize(addCartItemDTO.getSize());

        this.cartItemService.saveAndFlush(cartItem);
        return new Result(true, "Успешно добавихте този продукт във вашата количка!");
    }

    @Override
    public Result removeItemFromShoppingCart(User loggedUser, Long cartItemId) {
        ShoppingCart shoppingCart = loggedUser.getShoppingCart();

        boolean removed = shoppingCart.getCartItems().removeIf(cartItem ->
                cartItem.getId().equals(cartItemId));

        if (removed) {
            this.cartItemService.deleteById(cartItemId);
            this.shoppingCartRepository.saveAndFlush(shoppingCart);

            return new Result(true, "Продуктът беше успешно премахнат от количката.");
        }

        return new Result(false, "Продуктът не беше намерен в количката.");
    }

    @Override
    public void deleteItemsFromLoggedUserShoppingCartAfterOrder(User loggedUser) {
        List<CartItem> cartItems = new ArrayList<>(loggedUser.getShoppingCart().getCartItems());

        for (CartItem item : cartItems) {
            this.cartItemService.deleteById(item.getId());
        }

        loggedUser.getShoppingCart().getCartItems().clear();
        this.shoppingCartRepository.saveAndFlush(loggedUser.getShoppingCart());
    }

    @Override
    public void saveAndFlush(ShoppingCart shoppingCart) {
        this.shoppingCartRepository.saveAndFlush(shoppingCart);
    }
}