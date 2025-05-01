package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.User;

import java.math.BigDecimal;

public interface DiscountCardService {
    String checkForDiscountCard(User loggedUser, BigDecimal totalOutcome);
}
