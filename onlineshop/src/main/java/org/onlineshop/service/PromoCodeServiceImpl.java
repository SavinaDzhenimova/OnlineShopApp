package org.onlineshop.service;

import org.onlineshop.model.entity.PromoCode;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.PromoCodeDTO;
import org.onlineshop.model.exportDTO.PromoCodesListDTO;
import org.onlineshop.model.importDTO.AddPromoCodeDTO;
import org.onlineshop.repository.PromoCodeRepository;
import org.onlineshop.service.interfaces.PromoCodeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
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
}