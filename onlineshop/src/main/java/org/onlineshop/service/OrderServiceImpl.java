package org.onlineshop.service;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.*;
import org.onlineshop.model.enums.AddressType;
import org.onlineshop.model.enums.OrderStatus;
import org.onlineshop.model.enums.Region;
import org.onlineshop.model.enums.RoleName;
import org.onlineshop.model.exportDTO.OrderDTO;
import org.onlineshop.model.exportDTO.OrderItemDTO;
import org.onlineshop.model.exportDTO.OrderRequestDTO;
import org.onlineshop.model.importDTO.AddOrderDTO;
import org.onlineshop.model.importDTO.AddOrderItemDTO;
import org.onlineshop.model.importDTO.OrderItemRequestDTO;
import org.onlineshop.repository.OrderRepository;
import org.onlineshop.service.events.MakeOrderEvent;
import org.onlineshop.service.events.UpdateOrderStatusEvent;
import org.onlineshop.service.interfaces.*;
import org.onlineshop.service.utils.CurrentUserProvider;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PromoCodeService promoCodeService;
    private final CurrentUserProvider currentUserProvider;
    private final ShoeSizeService shoeSizeService;
    private final QuantitySizeService quantitySizeService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final AddressService addressService;
    private final ShoppingCartServiceLogged shoppingCartServiceLogged;
    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderServiceImpl(OrderRepository orderRepository, PromoCodeService promoCodeService,
                            CurrentUserProvider currentUserProvider, ShoeSizeService shoeSizeService,
                            QuantitySizeService quantitySizeService, CategoryService categoryService,
                            ProductService productService, AddressService addressService,
                            ShoppingCartServiceLogged shoppingCartServiceLogged,
                            ApplicationEventPublisher applicationEventPublisher) {
        this.orderRepository = orderRepository;
        this.promoCodeService = promoCodeService;
        this.currentUserProvider = currentUserProvider;
        this.shoeSizeService = shoeSizeService;
        this.quantitySizeService = quantitySizeService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.addressService = addressService;
        this.shoppingCartServiceLogged = shoppingCartServiceLogged;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return this.orderRepository.findAllOrderedByOrderedOnDesc(pageable)
                .map(this::mapOrderToDto);
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
        addOrderDTO.setFinalPrice((createdOrder.getFinalPrice().equals(BigDecimal.ZERO))
                ? createdOrder.getTotalPrice() : createdOrder.getFinalPrice());

        User loggedUser = this.currentUserProvider.getLoggedUser();

        if (loggedUser != null) {
            DiscountCard discountCard = loggedUser.getDiscountCard();

            if (discountCard != null) {
                BigDecimal discountValue = createdOrder.getTotalPrice()
                        .multiply(discountCard.getDiscountPercent().divide(BigDecimal.valueOf(100)));
                addOrderDTO.setVipStatusDiscount(discountValue);
                addOrderDTO.setFinalPrice(addOrderDTO.getFinalPrice().subtract(discountValue));
            }
        }

        return Optional.of(addOrderDTO);
    }

    @Override
    public Result makeOrder(AddOrderDTO addOrderDTO, HttpSession session) {
        if (addOrderDTO == null) {
            return new Result(false, "Поръчката, която се опитвате да направите, не съществува!");
        }

        Order order = new Order();

        order.setFullName(addOrderDTO.getFullName());
        order.setRegion(addOrderDTO.getRegion());
        order.setTown(addOrderDTO.getTown());
        order.setPostalCode(addOrderDTO.getPostalCode());
        order.setStreet(addOrderDTO.getStreet());
        order.setAddressType(addOrderDTO.getAddressType());
        order.setPhoneNumber(addOrderDTO.getPhoneNumber());
        order.setEmail(addOrderDTO.getEmail());
        order.setTotalPrice(addOrderDTO.getTotalPrice());
        order.setDiscount(addOrderDTO.getDiscount() != null ? addOrderDTO.getDiscount() : BigDecimal.ZERO);
        order.setVipStatusDiscount(addOrderDTO.getVipStatusDiscount() != null ? addOrderDTO.getVipStatusDiscount() : BigDecimal.ZERO);
        order.setFinalPrice(addOrderDTO.getFinalPrice() != null ? addOrderDTO.getFinalPrice() : addOrderDTO.getTotalPrice());
        order.setOrderedOn(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTrackingCode(UUID.randomUUID().toString());

        Optional<PromoCode> optionalPromoCode = this.promoCodeService.getByCode(addOrderDTO.getPromoCode());
        User loggedUser = this.currentUserProvider.getLoggedUser();

        optionalPromoCode.ifPresent(order::setPromoCode);

        this.orderRepository.saveAndFlush(order);

        if (loggedUser != null) {
            order.setUser(loggedUser);
            loggedUser.getOrders().add(order);

            BigDecimal loggedUserTotalOutcome = loggedUser.getTotalOutcome().add(order.getTotalPrice());
            loggedUser.setTotalOutcome(loggedUserTotalOutcome);

            this.addAddressToLoggedUserIfNotPresent(addOrderDTO, loggedUser);

            this.currentUserProvider.saveAndFlushUser(loggedUser);

            this.shoppingCartServiceLogged.deleteItemsFromLoggedUserShoppingCartAfterOrder(loggedUser);

        } else {
            ShoppingCart shoppingCart = (ShoppingCart) session.getAttribute("guestCart");

            if (shoppingCart != null && shoppingCart.getCartItems() != null) {
                shoppingCart.getCartItems().clear();
                session.removeAttribute("guestCart");
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

        String orderTrackingUrl = "/orders/track/" + order.getId() + "/" + order.getTrackingCode();
        String promoCodeName = order.getPromoCode() != null ? order.getPromoCode().getCode() : "";
        BigDecimal discountPercent = order.getPromoCode() != null ? order.getPromoCode().getDiscountValue() : BigDecimal.ZERO;
        String discountCardName = order.getUser() != null && order.getUser().getDiscountCard() != null
                ? order.getUser().getDiscountCard().getDiscountCardName().getDisplayName()
                : null;
        BigDecimal discountCardPercent = order.getUser() != null && order.getUser().getDiscountCard() != null
                ? order.getUser().getDiscountCard().getDiscountPercent()
                : BigDecimal.ZERO;

        this.applicationEventPublisher.publishEvent(
                new MakeOrderEvent(this, order.getFullName(), order.getEmail(),
                        this.mapAddressToString(order.getRegion(), order.getTown(), order.getPostalCode(),
                                order.getStreet(), order.getAddressType()),
                        order.getPhoneNumber(), order.getTotalPrice(), order.getDiscount(), promoCodeName,
                        discountPercent, order.getFinalPrice(), this.mapOrderStatusToString(order.getStatus()),
                        order.getOrderedOn(), discountCardName, discountCardPercent, order.getVipStatusDiscount(),
                        "http://localhost:8090" + orderTrackingUrl));

        return new Result(true, orderTrackingUrl);
    }

    private void addAddressToLoggedUserIfNotPresent(AddOrderDTO addOrderDTO, User loggedUser) {
        Address newAddressToAdd = new Address();

        newAddressToAdd.setRegion(addOrderDTO.getRegion());
        newAddressToAdd.setTown(addOrderDTO.getTown());
        newAddressToAdd.setPostalCode(addOrderDTO.getPostalCode());
        newAddressToAdd.setStreet(addOrderDTO.getStreet());
        newAddressToAdd.setAddressType(addOrderDTO.getAddressType());
        newAddressToAdd.setUser(loggedUser);

        boolean isAlreadyAdded = loggedUser.getAddresses().stream()
                .anyMatch(address -> address.equals(newAddressToAdd));

        if (!isAlreadyAdded) {
            this.addressService.saveAndFlush(newAddressToAdd);
            loggedUser.getAddresses().add(newAddressToAdd);
            this.currentUserProvider.saveAndFlushUser(loggedUser);
        }
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

        this.applicationEventPublisher.publishEvent(
                new UpdateOrderStatusEvent(this, order.getId(), order.getFullName(), order.getEmail(),
                        this.mapOrderStatusToString(orderStatus), this.mapOrderStatusToString(order.getStatus())));

        return new Result(true, "Успешно променихте статуса на поръчка №" + order.getId() +
                " от \"" + this.mapOrderStatusToString(orderStatus) +
                "\"" + " на \"" + this.mapOrderStatusToString(order.getStatus()) + "\"");
    }

    @Override
    public OrderDTO getOrderInfo(Long id, String trackingCode) {
        Optional<Order> optionalOrderById = this.orderRepository.findById(id);
        Optional<Order> optionalOrderByTrackingCode = this.orderRepository.findByTrackingCode(trackingCode);

        if (optionalOrderById.isEmpty() || optionalOrderByTrackingCode.isEmpty()) {
            throw new NoSuchElementException("Поръчката не съществува!");
        }

        if (!optionalOrderById.get().getId().equals(optionalOrderByTrackingCode.get().getId())) {
            throw new NoSuchElementException("Поръчката не съществува!");
        }

        Order order = optionalOrderById.get();
        User loggedUser = this.currentUserProvider.getLoggedUser();

        if (loggedUser != null && loggedUser.getRole().getRoleName().equals(RoleName.ADMIN)) {
            return this.mapOrderToDto(order);
        }

        if (loggedUser != null && order.getUser() != null && order.getUser().getEmail().equals(loggedUser.getEmail())) {
            return this.mapOrderToDto(order);
        }

        if (loggedUser == null && order.getUser() == null && order.getTrackingCode().equals(trackingCode)) {
            return this.mapOrderToDto(order);
        }

        throw new AccessDeniedException("Нямате достъп до тази поръчка.");
    }

    @Override
    public OrderDTO mapOrderToDto(Order order) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(order.getId());
        orderDTO.setFullName(order.getFullName());
        orderDTO.setEmail(order.getEmail());
        orderDTO.setPhoneNumber(order.getPhoneNumber());
        orderDTO.setTrackingCode(order.getTrackingCode());

        String deliveryAddress = this.mapAddressToString(order.getRegion(), order.getTown(), order.getPostalCode(),
                order.getStreet(), order.getAddressType());
        orderDTO.setDeliveryAddress(deliveryAddress);

        orderDTO.setOrderedOn(order.getOrderedOn());
        orderDTO.setStatus(this.mapOrderStatusToString(order.getStatus()));
        orderDTO.setStatusClass(this.mapStatusClass(order.getStatus()));
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setFinalPrice(order.getFinalPrice());
        orderDTO.setDiscount(order.getDiscount() != null ? order.getDiscount() : BigDecimal.ZERO);
        orderDTO.setVipStatusDiscount(order.getVipStatusDiscount() != null ? order.getVipStatusDiscount() : BigDecimal.ZERO);
        orderDTO.setDiscountCardName(order.getUser() != null && order.getUser().getDiscountCard() != null
                ? order.getUser().getDiscountCard().getDiscountCardName().getDisplayName()
                : "");
        orderDTO.setDiscountCardPercent(order.getUser() != null && order.getUser().getDiscountCard() != null
                ? order.getUser().getDiscountCard().getDiscountPercent()
                : BigDecimal.ZERO);
        orderDTO.setPromoCode(order.getPromoCode() != null ? order.getPromoCode().getCode() : "");
        orderDTO.setDiscountPercent(order.getPromoCode() != null ? order.getPromoCode().getDiscountValue() : BigDecimal.ZERO);

        List<OrderItemDTO> orderItemsList = order.getOrderItems().stream()
                .map(orderItem -> {
                    OrderItemDTO orderItemDTO = new OrderItemDTO();

                    orderItemDTO.setName(orderItem.getProduct().getName());
                    orderItemDTO.setImageUrl(orderItem.getProduct().getImages().get(0).getImageUrl());
                    orderItemDTO.setCategory(orderItem.getProduct().getCategory().getCategoryName().getDisplayName());
                    orderItemDTO.setSelectedSize(orderItem.getSize().getSize());
                    orderItemDTO.setSelectedQuantity(orderItem.getQuantity());
                    orderItemDTO.setUnitPrice(orderItem.getPrice());

                    return orderItemDTO;
                }).toList();

        orderDTO.setOrderItems(orderItemsList);

        return orderDTO;
    }

    private String mapAddressToString(Region region, String town, String postalCode, String street,
                                      AddressType addressType) {
        return region.getDisplayName() + ", " + town + " " + postalCode + ", " + street + " - "
                + addressType.getDisplayName();
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
