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
import org.onlineshop.service.interfaces.CategoryService;
import org.onlineshop.service.interfaces.ProductService;
import org.onlineshop.service.interfaces.ShoppingCartService;
import org.onlineshop.service.utils.CurrentUserProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemService cartItemService;
    private final CategoryService categoryService;
    private final CurrentUserProvider currentUserProvider;
    private final ProductService productService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, CartItemService cartItemService,
                                   CategoryService categoryService, CurrentUserProvider currentUserProvider,
                                   ProductService productService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartItemService = cartItemService;
        this.categoryService = categoryService;
        this.currentUserProvider = currentUserProvider;
        this.productService = productService;
    }

    @Transactional
    @Override
    public ShoppingCartDTO getCartItemsForCurrentUser(HttpSession session) {
        User loggedUser = this.currentUserProvider.getLoggedUser();

        ShoppingCart shoppingCart;

        if (loggedUser != null) {
            shoppingCart = loggedUser.getShoppingCart();
        } else {
            shoppingCart = (ShoppingCart) session.getAttribute("guestCart");
        }

        if (shoppingCart == null || shoppingCart.getCartItems() == null) {
            return new ShoppingCartDTO(new ArrayList<>(), BigDecimal.ZERO);
        }

        List<CartItemDTO> cartItemDTOList = shoppingCart.getCartItems().stream()
                .map(this::mapToCartItemDTO).toList();

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
        cartItemDTO.setName(product.getName());
        cartItemDTO.setImageUrl(product.getImages().get(0).getImageUrl());
        cartItemDTO.setCategories(this.categoryService.mapCategoriesToString(product.getCategories()));
        cartItemDTO.setSelectedQuantity(cartItem.getQuantity());
        cartItemDTO.setSelectedSize(cartItem.getSize());
        cartItemDTO.setPrice(product.getPrice());

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
        ShoppingCart shoppingCart;

        User loggedUser = this.currentUserProvider.getLoggedUser();

        if (loggedUser != null) {
            shoppingCart = loggedUser.getShoppingCart();
        } else {
            shoppingCart = (ShoppingCart) session.getAttribute("guestCart");

            if (shoppingCart == null) {
                shoppingCart = new ShoppingCart();
                shoppingCart.setCartItems(new ArrayList<>());
                session.setAttribute("guestCart", shoppingCart);
            }
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

        if (availableQuantity < requestedQuantity) {
            return new Result(false, "Наличното количество за размер " +
                    addCartItemDTO.getSize() + " е само " + availableQuantity + " броя.");
        }

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setProduct(product);
        cartItem.setQuantity(requestedQuantity);
        cartItem.setSize(addCartItemDTO.getSize());

        if (shoppingCart.getId() != null) {
            this.cartItemService.saveAndFlush(cartItem);
        } else {
            cartItem.setTempId(System.currentTimeMillis() + (long) (Math.random() * 100000));
            shoppingCart.getCartItems().add(cartItem);
        }

        return new Result(true, "Успешно добавихте този продукт във вашата количка!");
    }

    @Override
    public Result removeItemFromShoppingCart(Long cartItemId, HttpSession session) {

        User loggedUser = this.currentUserProvider.getLoggedUser();
        ShoppingCart shoppingCart = (loggedUser != null)
                ? loggedUser.getShoppingCart()
                : (ShoppingCart) session.getAttribute("guestCart");

        if (shoppingCart == null || shoppingCart.getCartItems() == null) {
            return new Result(false, "Няма активна количка.");
        }

        boolean removed = shoppingCart.getCartItems().removeIf(cartItem ->
                (cartItem.getId() != null && cartItem.getId().equals(cartItemId)) ||
                (cartItem.getId() == null && cartItem.getTempId() != null && cartItem.getTempId().equals(cartItemId)));

        if (removed) {
            this.cartItemService.deleteById(cartItemId);
            this.shoppingCartRepository.saveAndFlush(shoppingCart);

            return new Result(true, "Продуктът беше успешно премахнат от количката.");
        }

        return new Result(false, "Продуктът не беше намерен в количката.");
    }

    @Override
    public void saveAndFlush(ShoppingCart shoppingCart) {
        this.shoppingCartRepository.saveAndFlush(shoppingCart);
    }
}