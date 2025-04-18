package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Result;
import org.onlineshop.model.importDTO.AddPromoCodeDTO;

public interface PromoCodeService {

    Result addPromoCode(AddPromoCodeDTO addPromoCodeDTO);
}
