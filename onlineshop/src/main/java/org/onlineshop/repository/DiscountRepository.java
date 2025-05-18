package org.onlineshop.repository;

import org.onlineshop.model.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    List<Discount> findAllByValidFromLessThanEqualAndValidToGreaterThanEqual(LocalDate from, LocalDate to);
}