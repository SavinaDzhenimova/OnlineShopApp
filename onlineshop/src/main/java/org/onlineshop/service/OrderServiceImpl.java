package org.onlineshop.service;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.PromoCode;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.OrderRequestDTO;
import org.onlineshop.model.importDTO.AddOrderDTO;
import org.onlineshop.model.importDTO.AddOrderItemDTO;
import org.onlineshop.model.importDTO.OrderItemRequestDTO;
import org.onlineshop.repository.OrderRepository;
import org.onlineshop.service.interfaces.OrderService;
import org.onlineshop.service.interfaces.PromoCodeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PromoCodeService promoCodeService;

    public OrderServiceImpl(OrderRepository orderRepository, PromoCodeService promoCodeService) {
        this.orderRepository = orderRepository;
        this.promoCodeService = promoCodeService;
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
                    return addOrderItemDTO;
                }).toList();

        addOrderDTO.setOrderItems(orderItems);
        addOrderDTO.setTotalPrice(createdOrder.getTotalPrice());
        addOrderDTO.setDiscount(createdOrder.getDiscount());
        addOrderDTO.setFinalPrice(createdOrder.getFinalPrice());

        return Optional.of(addOrderDTO);
    }

    @Override
    public Result makeOrder(AddOrderDTO addOrderDTO) {

        

        return new Result(true, "Успешно направихте своята поръчка!");
    }
}
