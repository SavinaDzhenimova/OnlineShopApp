package org.onlineshop.service;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.*;
import org.onlineshop.model.enums.OrderStatus;
import org.onlineshop.model.exportDTO.OrderDTO;
import org.onlineshop.model.exportDTO.OrderItemDTO;
import org.onlineshop.model.exportDTO.OrderRequestDTO;
import org.onlineshop.model.importDTO.AddOrderDTO;
import org.onlineshop.model.importDTO.AddOrderItemDTO;
import org.onlineshop.model.importDTO.OrderItemRequestDTO;
import org.onlineshop.repository.OrderRepository;
import org.onlineshop.service.interfaces.*;
import org.onlineshop.service.utils.CurrentUserProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PromoCodeService promoCodeService;
    private final CurrentUserProvider currentUserProvider;
    private final ShoeSizeService shoeSizeService;
    private final QuantitySizeService quantitySizeService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ShoppingCartServiceLogged shoppingCartServiceLogged;
    private final ShoppingCartServiceGuest shoppingCartServiceGuest;

    public OrderServiceImpl(OrderRepository orderRepository, PromoCodeService promoCodeService,
                            CurrentUserProvider currentUserProvider, ShoeSizeService shoeSizeService,
                            QuantitySizeService quantitySizeService, CategoryService categoryService,
                            ProductService productService, ShoppingCartServiceLogged shoppingCartServiceLogged,
                            ShoppingCartServiceGuest shoppingCartServiceGuest) {
        this.orderRepository = orderRepository;
        this.promoCodeService = promoCodeService;
        this.currentUserProvider = currentUserProvider;
        this.shoeSizeService = shoeSizeService;
        this.quantitySizeService = quantitySizeService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.shoppingCartServiceLogged = shoppingCartServiceLogged;
        this.shoppingCartServiceGuest = shoppingCartServiceGuest;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = this.orderRepository.findAll();

        return orders.stream()
                .map(this::mapOrderToDto)
                .toList();
    }

    @Override
    public OrderRequestDTO createOrder(String promoCode, List<OrderItemRequestDTO> orderItems) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequestDTO item : orderItems) {
            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getSelectedQuantity()));
            total = total.add(itemTotal);
        }

        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal discountPercent = BigDecimal.ZERO;
        BigDecimal finalPrice = BigDecimal.ZERO;

        if (promoCode != null && !promoCode.trim().isEmpty()) {
            Optional<PromoCode> optionalPromoCode = this.promoCodeService.validatePromoCode(promoCode);

            if (optionalPromoCode.isPresent()) {
                PromoCode promo = optionalPromoCode.get();
                discountPercent = promo.getDiscountValue();
                discount = total.multiply(discountPercent).divide(BigDecimal.valueOf(100));
                finalPrice = total.subtract(discount);
            }
        }

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setOrderItems(orderItems);
        orderRequestDTO.setPromoCode(promoCode);
        orderRequestDTO.setTotalPrice(total);
        orderRequestDTO.setDiscount(discount);
        orderRequestDTO.setDiscountPercent(discountPercent);
        orderRequestDTO.setFinalPrice(finalPrice);

        return orderRequestDTO;
    }

    @Override
    public Optional<AddOrderDTO> prepareAddOrderDTOFromSession(HttpSession session) {
        OrderRequestDTO createdOrder = (OrderRequestDTO) session.getAttribute("createdOrder");

        if (createdOrder == null) {
            return Optional.empty();
        }

        AddOrderDTO addOrderDTO = new AddOrderDTO();

        List<AddOrderItemDTO> orderItems = createdOrder.getOrderItems().stream()
                .map(orderItem -> {
                    AddOrderItemDTO addOrderItemDTO = new AddOrderItemDTO();

                    addOrderItemDTO.setSelectedSize(orderItem.getSelectedSize());
                    addOrderItemDTO.setSelectedQuantity(orderItem.getSelectedQuantity());
                    addOrderItemDTO.setUnitPrice(orderItem.getUnitPrice());
                    addOrderItemDTO.setProductId(orderItem.getProductId());

                    return addOrderItemDTO;
                }).toList();

        addOrderDTO.setOrderItems(orderItems);
        addOrderDTO.setTotalPrice(createdOrder.getTotalPrice());
        addOrderDTO.setPromoCode(createdOrder.getPromoCode());
        addOrderDTO.setDiscountPercent(createdOrder.getDiscountPercent());
        addOrderDTO.setDiscount(createdOrder.getDiscount());
        addOrderDTO.setFinalPrice(createdOrder.getFinalPrice());

        return Optional.of(addOrderDTO);
    }

    @Override
    public Result makeOrder(AddOrderDTO addOrderDTO, HttpSession session) {
        if (addOrderDTO == null) {
            return new Result(false, "Поръчката, която се опитвате да направите, не съществува!");
        }

        Order order = new Order();

        order.setFullName(addOrderDTO.getFullName());
        order.setDeliveryAddress(addOrderDTO.getAddress());
        order.setPhoneNumber(addOrderDTO.getPhoneNumber());
        order.setEmail(addOrderDTO.getEmail());
        order.setTotalPrice(addOrderDTO.getTotalPrice());
        order.setDiscount(addOrderDTO.getDiscount() != null ? addOrderDTO.getDiscount() : BigDecimal.ZERO);
        order.setFinalPrice(addOrderDTO.getFinalPrice() != null ? addOrderDTO.getFinalPrice() : addOrderDTO.getTotalPrice());
        order.setOrderedOn(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        Optional<PromoCode> optionalPromoCode = this.promoCodeService.getByCode(addOrderDTO.getPromoCode());
        User loggedUser = this.currentUserProvider.getLoggedUser();

        optionalPromoCode.ifPresent(order::setPromoCode);

        this.orderRepository.saveAndFlush(order);

        if (loggedUser != null) {
            order.setUser(loggedUser);
            loggedUser.getOrders().add(order);

            BigDecimal loggedUserTotalOutcome = loggedUser.getTotalOutcome().add(order.getTotalPrice());
            loggedUser.setTotalOutcome(loggedUserTotalOutcome);

            this.currentUserProvider.saveAndFlushUser(loggedUser);

            List<Long> idsToRemove = loggedUser.getShoppingCart().getCartItems().stream()
                    .map(BaseEntity::getId).toList();

            idsToRemove.forEach(id -> this.shoppingCartServiceLogged.removeItemFromShoppingCart(loggedUser, id));

        } else {
            ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("guestCart");

            if (shoppingCart != null) {
                List<Long> idsToRemove = shoppingCart.getCartItems().stream()
                        .map(CartItem::getTempId).toList();

                idsToRemove.forEach(id -> this.shoppingCartServiceGuest.removeItemFromShoppingCart(id, session));
            }
        }

        List<OrderItem> orderItems = addOrderDTO.getOrderItems().stream()
                .map(addOrderItemDTO -> {
                    OrderItem orderItem = new OrderItem();

                    Optional<ShoeSize> optionalShoeSize = this.shoeSizeService.getBySize(addOrderItemDTO.getSelectedSize());
                    if (optionalShoeSize.isEmpty()) {
                        throw new IllegalArgumentException("Избраният размер не съществува!");
                    }

                    Optional<Product> optionalProduct = this.productService.getById(addOrderItemDTO.getProductId());
                    if (optionalProduct.isEmpty()) {
                        throw new IllegalArgumentException("Избраният продукт не съществува!");
                    }

                    this.updateOrderedProductQuantity(optionalProduct.get(), addOrderItemDTO);

                    orderItem.setSize(optionalShoeSize.get());
                    orderItem.setQuantity(addOrderItemDTO.getSelectedQuantity());
                    orderItem.setPrice(addOrderItemDTO.getUnitPrice());
                    orderItem.setProduct(optionalProduct.get());
                    orderItem.setOrder(order);

                    return orderItem;
                }).toList();


        order.setOrderItems(orderItems);

        this.orderRepository.saveAndFlush(order);

        String orderTrackingUrl = "/orders/track/" + order.getId();
        return new Result(true, orderTrackingUrl);
    }

    private void updateOrderedProductQuantity(Product product, AddOrderItemDTO addOrderItemDTO) {
        Optional<QuantitySize> optionalQuantitySize = product.getQuantitySize().stream()
                .filter(quantitySize -> quantitySize.getProduct().getId() == product.getId()
                        && quantitySize.getSize().getSize() == addOrderItemDTO.getSelectedSize())
                .findFirst();

        if (optionalQuantitySize.isEmpty()) {
            throw new IllegalArgumentException("Избраният размер и продукт не съществуват!");
        }

        QuantitySize quantitySize = optionalQuantitySize.get();

        int newQuantity = optionalQuantitySize.get().getQuantity() - addOrderItemDTO.getSelectedQuantity();
        quantitySize.setQuantity(newQuantity);

        this.quantitySizeService.saveAndFlush(quantitySize);
    }

    @Override
    public Result updateOrderStatus(Long id) {
        Optional<Order> optionalOrder = this.orderRepository.findById(id);

        if (optionalOrder.isEmpty()) {
            return new Result(false, "Поръчката, която се опитвате да актуализирате, не съществува!");
        }

        Order order = optionalOrder.get();
        OrderStatus orderStatus = order.getStatus();

        if (orderStatus.equals(OrderStatus.DELIVERED) || orderStatus.equals(OrderStatus.CANCELLED)
                || orderStatus.equals(OrderStatus.RETURNED)) {

            return new Result(false, "Статусът на тази поръчка не може да бъде обновен!");
        }

        switch (orderStatus) {
            case PENDING -> order.setStatus(OrderStatus.PROCESSING);
            case PROCESSING -> order.setStatus(OrderStatus.SHIPPED);
            case SHIPPED -> order.setStatus(OrderStatus.DELIVERED);
        }

        this.orderRepository.saveAndFlush(order);

        return new Result(true, "Успешно променихте статуса на поръчка №" + order.getId() +
                " от \"" + this.mapOrderStatusToString(orderStatus) + 
                "\"" + " на \"" + this.mapOrderStatusToString(order.getStatus()) + "\"");
    }

    @Override
    public OrderDTO getOrderInfo(Long id) {
        Optional<Order> optionalOrder = this.orderRepository.findById(id);

        if (optionalOrder.isEmpty()) {
            return null;
        }

        Order order = optionalOrder.get();

        return this.mapOrderToDto(order);
    }

    @Override
    public OrderDTO mapOrderToDto(Order order) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(order.getId());
        orderDTO.setFullName(order.getFullName());
        orderDTO.setEmail(order.getEmail());
        orderDTO.setPhoneNumber(order.getPhoneNumber());
        orderDTO.setDeliveryAddress(order.getDeliveryAddress());
        orderDTO.setOrderedOn(order.getOrderedOn());
        orderDTO.setStatus(this.mapOrderStatusToString(order.getStatus()));
        orderDTO.setStatusClass(this.mapStatusClass(order.getStatus()));
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setFinalPrice(order.getFinalPrice());
        orderDTO.setDiscount(order.getDiscount() != null ? order.getDiscount() : BigDecimal.ZERO);
        orderDTO.setPromoCode(order.getPromoCode() != null ? order.getPromoCode().getCode() : "");
        orderDTO.setDiscountPercent(order.getPromoCode() != null ? order.getPromoCode().getDiscountValue() : BigDecimal.ZERO);

        List<OrderItemDTO> orderItemsList = order.getOrderItems().stream()
                .map(orderItem -> {
                    OrderItemDTO orderItemDTO = new OrderItemDTO();

                    orderItemDTO.setName(orderItem.getProduct().getName());
                    orderItemDTO.setImageUrl(orderItem.getProduct().getImages().get(0).getImageUrl());
                    orderItemDTO.setCategories(
                            this.categoryService.mapCategoriesToString(orderItem.getProduct().getCategories()));
                    orderItemDTO.setSelectedSize(orderItem.getSize().getSize());
                    orderItemDTO.setSelectedQuantity(orderItem.getQuantity());
                    orderItemDTO.setUnitPrice(orderItem.getPrice());

                    return orderItemDTO;
                }).toList();

        orderDTO.setOrderItems(orderItemsList);

        return orderDTO;
    }

    private String mapStatusClass(OrderStatus status) {
        String statusClass = "";

        switch (status) {
            case PENDING -> statusClass = "pending";
            case PROCESSING -> statusClass = "processing";
            case SHIPPED -> statusClass = "shipped";
            case DELIVERED -> statusClass = "delivered";
            case CANCELLED -> statusClass = "cancelled";
            case RETURNED -> statusClass = "returned";
        }

        return statusClass;
    }

    private String mapOrderStatusToString(OrderStatus status) {
        String statusStr = "";

        switch (status) {
            case PENDING -> statusStr = "Приета";
            case PROCESSING -> statusStr = "В процес на обработка";
            case SHIPPED -> statusStr = "Изпратена";
            case DELIVERED -> statusStr = "Доставена";
            case CANCELLED -> statusStr = "Отказана";
            case RETURNED -> statusStr = "Върната";
        }

        return statusStr;
    }

    @Override
    public Optional<Order> getById(Long id) {
        return this.orderRepository.findById(id);
    }
}
