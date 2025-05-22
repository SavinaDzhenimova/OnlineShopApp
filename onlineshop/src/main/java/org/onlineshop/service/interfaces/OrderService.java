package org.onlineshop.service.interfaces;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.Order;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.OrderDTO;
import org.onlineshop.model.exportDTO.OrderRequestDTO;
import org.onlineshop.model.importDTO.AddOrderDTO;
import org.onlineshop.model.importDTO.OrderItemRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderRequestDTO createOrder(String promoCode, List<OrderItemRequestDTO> items);

    Optional<AddOrderDTO> prepareAddOrderDTOFromSession(HttpSession session);

    Result makeOrder(AddOrderDTO addOrderDTO, HttpSession httpSession);

    OrderDTO getOrderInfo(Long id, String trackingCode);

    OrderDTO mapOrderToDto(Order order);

    Optional<Order> getById(Long id);

    Page<OrderDTO> getAllOrders(Pageable pageable);

    Result updateOrderStatus(Long id);
}
