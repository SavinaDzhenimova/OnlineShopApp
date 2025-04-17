package org.onlineshop.repository;

import org.onlineshop.model.entity.ShoeSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoeSizeRepository extends JpaRepository<ShoeSize, Long> {

    Optional<ShoeSize> findBySize(Integer size);
}
