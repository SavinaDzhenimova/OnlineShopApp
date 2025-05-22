package org.onlineshop.repository;

import org.onlineshop.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByTrackingCode(String trackingCode);

    @Query("SELECT o FROM Order o ORDER BY o.orderedOn DESC")
    Page<Order> findAllOrderedByOrderedOnDesc(Pageable pageable);
}