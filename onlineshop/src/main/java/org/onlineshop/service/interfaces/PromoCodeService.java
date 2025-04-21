package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.PromoCode;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.PromoCodesListDTO;
import org.onlineshop.model.importDTO.AddPromoCodeDTO;
import org.onlineshop.model.importDTO.CartItemRequestDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PromoCodeService {

    Result addPromoCode(AddPromoCodeDTO addPromoCodeDTO);

    PromoCodesListDTO getAllPromoCodes();

    Result deletePromoCode(Long id);

    Map<String, Object> applyPromoCode(String promoCode, List<CartItemRequestDTO> cartItems);

    Optional<PromoCode> getByCode(String code);
}
