package org.onlineshop.repository;

import org.onlineshop.model.entity.QuantitySize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantitySizeRepository extends JpaRepository<QuantitySize, Long> {

}