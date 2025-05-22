package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Product;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.enums.BrandName;
import org.onlineshop.model.exportDTO.ProductDTO;
import org.onlineshop.model.importDTO.AddProductDTO;
import org.onlineshop.model.importDTO.UpdateProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Result addProduct(AddProductDTO addProductDTO);

    ProductDTO mapProductToDTO(Product product);

    ProductDTO getProductInfo(Long id);

    Page<ProductDTO> getAllProducts(Pageable pageable);

    Page<ProductDTO> getFilteredProducts(List<Integer> sizes, List<BrandName> brands, List<String> colors,
                                         BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<ProductDTO> getProductsByCategory(String category, Pageable pageable);

    Page<ProductDTO> getProductsByBrand(String brand, Pageable pageable);

    Page<ProductDTO> getProductsByShoeSize(int size, Pageable pageable);

    Page<ProductDTO> getNewProducts(Pageable pageable);

    Page<ProductDTO> getProductsOnSale(Pageable pageable);

    Optional<Product> getById(Long id);

    List<Product> getAll();

    void saveAndFlush(Product product);

    List<ProductDTO> getNewProductsForIndexPage();

    List<ProductDTO> getProductsOnSaleForIndexPage();

    Result updateProductQuantityAndSizes(Long id, UpdateProductDTO updateProductDTO);
}