package org.onlineshop.repository;

import org.onlineshop.model.entity.DiscountCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountCardRepository extends JpaRepository<DiscountCard, Long> {
}
