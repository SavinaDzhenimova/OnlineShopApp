package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Result;
import org.onlineshop.model.importDTO.AddDiscountDTO;

public interface DiscountService {

    Result addDiscount(AddDiscountDTO addDiscountDTO);

    void applyDiscounts();
}