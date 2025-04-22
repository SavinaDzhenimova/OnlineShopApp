package org.onlineshop.service.interfaces;

import jakarta.servlet.http.HttpSession;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.OrderRequestDTO;
import org.onlineshop.model.importDTO.AddOrderDTO;
import org.onlineshop.model.importDTO.OrderItemRequestDTO;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    OrderRequestDTO createOrder(String promoCode, List<OrderItemRequestDTO> items);

    Optional<AddOrderDTO> prepareAddOrderDTOFromSession(HttpSession session);

    Result makeOrder(AddOrderDTO addOrderDTO);
}
