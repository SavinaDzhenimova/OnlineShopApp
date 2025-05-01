package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.User;
import org.onlineshop.model.exportDTO.DiscountCardDTO;

import java.math.BigDecimal;

public interface DiscountCardService {
    String checkForDiscountCard(User loggedUser, BigDecimal totalOutcome);

    DiscountCardDTO getDiscountCard(User loggedUser, BigDecimal orderTotalPrice);
}
