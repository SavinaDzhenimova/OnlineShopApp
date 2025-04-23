package org.onlineshop.service;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.*;
import org.onlineshop.model.exportDTO.CartItemDTO;
import org.onlineshop.model.exportDTO.ShoppingCartDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;
import org.onlineshop.model.importDTO.QuantitySizeDTO;
import org.onlineshop.service.interfaces.CategoryService;
import org.onlineshop.service.interfaces.ProductService;
import org.onlineshop.service.interfaces.ShoppingCartServiceGuest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceGuestImpl implements ShoppingCartServiceGuest {

    private final CategoryService categoryService;
    private final ProductService productService;

    public ShoppingCartServiceGuestImpl(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @Override
    public ShoppingCartDTO getCartItemsForCurrentUser(HttpSession session) {
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("guestCart");

        if (shoppingCart == null || shoppingCart.getCartItems() == null || shoppingCart.getCartItems().isEmpty()) {
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
        if (cartItem.getTempId() != null) {
            cartItemDTO.setTempId(cartItem.getTempId());
        }

        cartItemDTO.setName(product.getName());
        cartItemDTO.setImageUrl(product.getImages().get(0).getImageUrl());
        cartItemDTO.setCategories(this.categoryService.mapCategoriesToString(product.getCategories()));
        cartItemDTO.setSelectedQuantity(cartItem.getQuantity());
        cartItemDTO.setSelectedSize(cartItem.getSize());
        cartItemDTO.setUnitPrice(product.getPrice());
        cartItemDTO.setQuantityPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        cartItemDTO.setProductId(product.getId());

        Set<QuantitySizeDTO> quantitySizes = new TreeSet<>(product.getQuantitySize().stream()
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
    public Result addProductToShoppingCart(Long productId, AddCartItemDTO addCartItemDTO, HttpSession session) {
        addCartItemDTO.setProductId(productId);
        Optional<Product> optionalProduct = this.productService.getById(addCartItemDTO.getProductId());

        if (optionalProduct.isEmpty()) {
            return new Result(false, "Продуктът, който се опитвате да добавите в количката, не съществува!");
        }

        Product product = optionalProduct.get();
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("guestCart");

        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setCartItems(new ArrayList<>());
            session.setAttribute("guestCart", shoppingCart);
        }

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

        int availableQuantity = matchingSize.get().getQuantity();
        int requestedQuantity = addCartItemDTO.getQuantity();

        Optional<CartItem> existingCartItemOpt = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(product.getId())
                        && addCartItemDTO.getSize().equals(cartItem.getSize()))
                .findFirst();

        if (existingCartItemOpt.isPresent()) {
            CartItem existingCartItem = existingCartItemOpt.get();
            int newQuantity = existingCartItem.getQuantity() + requestedQuantity;

            if (newQuantity > availableQuantity) {
                return new Result(false, "Наличността за размер " + addCartItemDTO.getSize() +
                        " е само " + availableQuantity + " броя.");
            }

            existingCartItem.setQuantity(newQuantity);
            return new Result(true, "Актуализирахме количеството на този продукт във вашата количка!");
        }

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setProduct(product);
        cartItem.setQuantity(requestedQuantity);
        cartItem.setSize(addCartItemDTO.getSize());
        cartItem.setTempId(System.currentTimeMillis() + (long) (Math.random() * 100000));

        shoppingCart.getCartItems().add(cartItem);

        return new Result(true, "Успешно добавихте този продукт във вашата количка!");
    }

    @Override
    public Result removeItemFromShoppingCart(Long cartItemId, HttpSession session) {
        ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("guestCart");

        if (shoppingCart == null || shoppingCart.getCartItems() == null || shoppingCart.getCartItems().isEmpty()) {
            return new Result(false, "Няма активна количка.");
        }

        boolean removed = shoppingCart.getCartItems().removeIf(cartItem ->
                cartItem.getTempId() != null && cartItem.getTempId().equals(cartItemId));

        if (removed) {
            return new Result(true, "Продуктът беше успешно премахнат от количката.");
        }

        return new Result(false, "Продуктът не беше намерен в количката.");
    }
}