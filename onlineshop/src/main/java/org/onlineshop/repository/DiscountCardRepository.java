package org.onlineshop.repository;

import org.onlineshop.model.entity.DiscountCard;
import org.onlineshop.model.enums.DiscountCardName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountCardRepository extends JpaRepository<DiscountCard, Long> {

    Optional<DiscountCard> findByDiscountCardName(DiscountCardName discountCardName);
}
