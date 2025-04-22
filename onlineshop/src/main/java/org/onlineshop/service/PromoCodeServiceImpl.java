package org.onlineshop.service;

import org.onlineshop.model.entity.PromoCode;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.PromoCodeDTO;
import org.onlineshop.model.exportDTO.PromoCodesListDTO;
import org.onlineshop.model.importDTO.AddPromoCodeDTO;
import org.onlineshop.model.importDTO.CartItemRequestDTO;
import org.onlineshop.repository.PromoCodeRepository;
import org.onlineshop.service.interfaces.PromoCodeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeServiceImpl(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    @Override
    public PromoCodesListDTO getAllPromoCodes() {

        List<PromoCode> promoCodes = this.promoCodeRepository.findAll();

        List<PromoCodeDTO> promoCodeDTOList = promoCodes.stream()
                .map(promoCode -> {
                    PromoCodeDTO promoCodeDTO = new PromoCodeDTO();

                    promoCodeDTO.setId(promoCode.getId());
                    promoCodeDTO.setCode(promoCode.getCode());
                    promoCodeDTO.setDiscountValue(promoCode.getDiscountValue());
                    promoCodeDTO.setValidFrom(promoCode.getValidFrom());
                    promoCodeDTO.setValidTo(promoCode.getValidTo());
                    promoCodeDTO.setActive(promoCode.isActive());

                    return promoCodeDTO;
                }).toList();

        return new PromoCodesListDTO(promoCodeDTOList);
    }

    @Override
    public Result addPromoCode(AddPromoCodeDTO addPromoCodeDTO) {

        Optional<PromoCode> optionalPromoCode = this.promoCodeRepository.findByCode(addPromoCodeDTO.getCode());

        if (optionalPromoCode.isPresent()) {
            return new Result(false, "Промо кодът, който се опитате да добавите, вече същеструва!");
        }

        if (addPromoCodeDTO.getValidFrom().isAfter(addPromoCodeDTO.getValidTo())) {
            return new Result(false,
                    "Датата на изтичане на кода не може да бъде преди датата, от която е активен!");
        }

        PromoCode promoCode = new PromoCode();

        promoCode.setCode(addPromoCodeDTO.getCode());
        promoCode.setDiscountValue(addPromoCodeDTO.getDiscountValue());
        promoCode.setValidFrom(addPromoCodeDTO.getValidFrom());
        promoCode.setValidTo(addPromoCodeDTO.getValidTo());

        if (addPromoCodeDTO.getValidFrom().equals(LocalDate.now())) {
            promoCode.setActive(true);
        }

        this.promoCodeRepository.saveAndFlush(promoCode);
        return new Result(true, "Промо кодът е добавен успешно!");
    }

    @Override
    public Result deletePromoCode(Long id) {

        Optional<PromoCode> optionalPromoCode = this.promoCodeRepository.findById(id);

        if (optionalPromoCode.isEmpty()) {
            return new Result(false, "Промо кодът, който се опитвате да премахнете, не съществува!");
        }

        if (optionalPromoCode.get().isActive()) {
            return new Result(false, "Не можете да изтриете активен промо код!");
        }

        this.promoCodeRepository.deleteById(id);

        Optional<PromoCode> optionalPromoCodeAfterDeletion = this.promoCodeRepository.findById(id);

        if (optionalPromoCodeAfterDeletion.isPresent()) {
            return new Result(false, "Промо кодът не можа да бъде премахнат!");
        }

        return new Result(true, "Успешно премахнахте този промо код!");
    }

    @Override
    public Map<String, Object> applyPromoCode(String promoCode, List<CartItemRequestDTO> cartItems) {
        BigDecimal total = BigDecimal.ZERO;

        for (CartItemRequestDTO item : cartItems) {
            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getSelectedQuantity()));
            total = total.add(itemTotal);
        }

        BigDecimal discount = BigDecimal.ZERO;

        Optional<PromoCode> optionalPromo = promoCodeRepository.findByCode(promoCode);

        if (optionalPromo.isEmpty()) {
            return buildInvalidResponse(total, discount);
        }

        PromoCode promo = optionalPromo.get();

        if (!promo.isActive() || !promo.getCode().equals(promoCode)) {
            return buildInvalidResponse(total, discount);
        }

        BigDecimal discountPercent = promo.getDiscountValue();
        discount = total.multiply(discountPercent).divide(BigDecimal.valueOf(100));

        BigDecimal finalPrice = total.subtract(discount);

        Map<String, Object> response = new HashMap<>();
        response.put("originalPrice", total);
        response.put("discount", discount);
        response.put("finalPrice", finalPrice);
        response.put("discountPercent", discountPercent);

        return response;
    }

    private Map<String, Object> buildInvalidResponse(BigDecimal total, BigDecimal discount) {
        Map<String, Object> invalidResponse = new HashMap<>();
        invalidResponse.put("error", "Невалиден промо код!");
        invalidResponse.put("discount", discount);
        invalidResponse.put("finalPrice", total);
        invalidResponse.put("originalPrice", total);
        return invalidResponse;
    }

    @Override
    public Optional<PromoCode> validatePromoCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }

        String trimmedCode = code.trim();

        return promoCodeRepository.findByCode(trimmedCode)
                .filter(promo -> promo.getCode().equals(trimmedCode))
                .filter(PromoCode::isActive)
                .filter(promo -> {
                    LocalDate today = LocalDate.now();
                    return (promo.getValidFrom() == null || !today.isBefore(promo.getValidFrom())) &&
                            (promo.getValidTo() == null || !today.isAfter(promo.getValidTo()));
                });
    }

    @Override
    public Optional<PromoCode> getByCode(String code) {
        return this.promoCodeRepository.findByCode(code);
    }
}