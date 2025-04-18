package org.onlineshop.service;

import org.onlineshop.model.entity.PromoCode;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.importDTO.AddPromoCodeDTO;
import org.onlineshop.repository.PromoCodeRepository;
import org.onlineshop.service.interfaces.PromoCodeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeServiceImpl(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
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
}