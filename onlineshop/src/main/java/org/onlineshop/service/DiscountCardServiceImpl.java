package org.onlineshop.service;

import org.onlineshop.model.entity.DiscountCard;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.enums.DiscountCardName;
import org.onlineshop.repository.DiscountCardRepository;
import org.onlineshop.service.interfaces.DiscountCardService;
import org.onlineshop.service.interfaces.UserService;
import org.onlineshop.service.utils.CurrentUserProvider;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class DiscountCardServiceImpl implements DiscountCardService {

    private final UserService userService;
    private final DiscountCardRepository discountCardRepository;

    public DiscountCardServiceImpl(UserService userService, DiscountCardRepository discountCardRepository) {
        this.userService = userService;
        this.discountCardRepository = discountCardRepository;
    }

    @Override
    public String checkForDiscountCard(User loggedUser, BigDecimal totalOutcome) {

        Optional<DiscountCard> optionalDiscountCard = Optional.empty();
        String message = "";

        if (totalOutcome.compareTo(BigDecimal.valueOf(1201)) >= 0
                && loggedUser.getDiscountCard().getDiscountCardName().equals(DiscountCardName.VIP_700)) {
             optionalDiscountCard = this.discountCardRepository.findByDiscountCardName(DiscountCardName.VIP_1200);
             message = "Поздравления! Вие достигнахте ниво ВИП 700! Вече ще можете да се възползвате от 10% " +
                     "допълнителна отстъпка при всяка следваща поръчка!";
        } else if (totalOutcome.compareTo(BigDecimal.valueOf(701)) >= 0
                && loggedUser.getDiscountCard().getDiscountCardName().equals(DiscountCardName.VIP_300)) {
            optionalDiscountCard = this.discountCardRepository.findByDiscountCardName(DiscountCardName.VIP_700);
            message = "Поздравления! Вие достигнахте ниво ВИП 700! Вече ще можете да се възползвате от 7% " +
                    "допълнителна отстъпка при всяка следваща поръчка!";
        } else if (totalOutcome.compareTo(BigDecimal.valueOf(300)) >= 0
                && loggedUser.getDiscountCard() == null) {
            optionalDiscountCard = this.discountCardRepository.findByDiscountCardName(DiscountCardName.VIP_300);
            message = "Поздравления! Вие достигнахте ниво ВИП 300! Вече ще можете да се възползвате от 5%" +
                    " допълнителна отстъпка при всяка следваща поръчка!";
        }

        if (optionalDiscountCard.isEmpty()) {
            return message;
        }

        loggedUser.setDiscountCard(optionalDiscountCard.get());

        this.userService.saveAndFlushUser(loggedUser);

        return message;
    }
}