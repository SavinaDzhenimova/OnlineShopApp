package org.onlineshop.repository;

import org.onlineshop.model.entity.Product;
import org.onlineshop.model.enums.BrandName;
import org.onlineshop.model.enums.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByIsNewTrueAndAddedOnBefore(LocalDate date);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.categoryName = :categoryName")
    List<Product> findAllByCategoryName(@Param("categoryName") CategoryName categoryName);

    @Query("SELECT p FROM Product p JOIN p.brand b WHERE b.brandName = :brandName")
    List<Product> findAllByBrandName(@Param("brandName") BrandName brandName);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.quantitySize qs WHERE qs.size.size = :size AND qs.quantity > 0")
    List<Product> findAllBySizeWithStock(int size);
}