package org.onlineshop.service;

import org.onlineshop.model.entity.DiscountCard;
import org.onlineshop.model.entity.User;
import org.onlineshop.model.enums.DiscountCardName;
import org.onlineshop.model.exportDTO.DiscountCardDTO;
import org.onlineshop.repository.DiscountCardRepository;
import org.onlineshop.service.interfaces.DiscountCardService;
import org.onlineshop.service.interfaces.UserService;
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

    @Override
    public DiscountCardDTO getDiscountCard(User loggedUser, BigDecimal orderTotalPrice) {
        DiscountCardDTO discountCardDTO = new DiscountCardDTO();

        if (loggedUser != null && loggedUser.getDiscountCard() != null) {
            DiscountCard discountCard = loggedUser.getDiscountCard();

            discountCardDTO.setDiscountCardName(discountCard.getDiscountCardName().getDisplayName());
            discountCardDTO.setDiscountPercent(discountCard.getDiscountPercent());

            BigDecimal discountValue = orderTotalPrice.multiply(discountCard.getDiscountPercent()
                    .divide(BigDecimal.valueOf(100)));
            discountCardDTO.setDiscountValue(discountValue);

            return discountCardDTO;
        }

        return null;
    }
}