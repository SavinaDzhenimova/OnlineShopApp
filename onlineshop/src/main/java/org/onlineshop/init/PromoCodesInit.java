package org.onlineshop.init;

import org.onlineshop.model.entity.PromoCode;
import org.onlineshop.repository.PromoCodeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PromoCodesInit implements CommandLineRunner {

    private final PromoCodeRepository promoCodeRepository;

    public PromoCodesInit(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (this.promoCodeRepository.count() == 0) {
            PromoCode gift = new PromoCode();
            gift.setCode("GIFT5");
            gift.setDiscountValue(BigDecimal.valueOf(5));
            gift.setActive(true);

            this.promoCodeRepository.saveAndFlush(gift);

            PromoCode news = new PromoCode();
            news.setCode("NEWS5");
            news.setDiscountValue(BigDecimal.valueOf(5));
            news.setActive(true);

            this.promoCodeRepository.saveAndFlush(news);
        }
    }
}