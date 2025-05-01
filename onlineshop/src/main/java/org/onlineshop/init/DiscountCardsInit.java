package org.onlineshop.init;

import org.onlineshop.model.entity.DiscountCard;
import org.onlineshop.model.enums.DiscountCardName;
import org.onlineshop.repository.DiscountCardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DiscountCardsInit implements CommandLineRunner {

    private final DiscountCardRepository discountCardRepository;

    public DiscountCardsInit(DiscountCardRepository discountCardRepository) {
        this.discountCardRepository = discountCardRepository;
    }

    @Override
    public void run(String... args) {

        if (this.discountCardRepository.count() == 0) {

            Arrays.stream(DiscountCardName.values())
                    .forEach(discountCardName -> {
                        DiscountCard discountCard = new DiscountCard();
                        discountCard.setDiscountCardName(discountCardName);

                        String description = switch (discountCardName) {
                            case VIP_300 -> "При изпратени поръчки на стойност от 300 лв. до 700 лв. получаваш 5% допълнителна отстъпка.";
                            case VIP_700 -> "При изпратени поръчки на стойност от 701 лв. до 1200 лв. получаваш 7% допълнителна отстъпка.";
                            case VIP_1200 -> "При изпратени поръчки на стойност над 1201 лв. получаваш 10% допълнителна отстъпка.";
                        };

                        BigDecimal discountPercent = switch (discountCardName) {
                            case VIP_300 -> BigDecimal.valueOf(5);
                            case VIP_700 -> BigDecimal.valueOf(7);
                            case VIP_1200 -> BigDecimal.valueOf(10);
                        };

                        discountCard.setDescription(description);
                        discountCard.setDiscountPercent(discountPercent);
                        this.discountCardRepository.saveAndFlush(discountCard);
                    });
        }
    }
}
