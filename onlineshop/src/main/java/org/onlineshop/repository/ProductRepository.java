package org.onlineshop.repository;

import org.onlineshop.model.entity.Product;
import org.onlineshop.model.enums.BrandName;
import org.onlineshop.model.enums.CategoryName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findAllByIsNewTrue();

    Page<Product> findAllByIsNewTrue(Pageable pageable);

    List<Product> findAllByIsNewTrueAndAddedOnBefore(LocalDate date);

    Page<Product> findAllByIsOnSaleTrue(Pageable pageable);

    List<Product> findAllByIsOnSaleTrue();

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.categoryName = :categoryName")
    Page<Product> findAllByCategoryName(@Param("categoryName") CategoryName categoryName, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.brand b WHERE b.brandName = :brandName")
    Page<Product> findAllByBrandName(@Param("brandName") BrandName brandName, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.quantitySize qs WHERE qs.size.size = :size AND qs.quantity > 0")
    Page<Product> findAllBySizeWithStock(int size, Pageable pageable);
}