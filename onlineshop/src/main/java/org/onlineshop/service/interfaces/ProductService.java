package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Product;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.enums.BrandName;
import org.onlineshop.model.exportDTO.ProductDTO;
import org.onlineshop.model.exportDTO.ProductsListDTO;
import org.onlineshop.model.importDTO.AddProductDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Result addProduct(AddProductDTO addProductDTO);

    ProductDTO mapProductToDTO(Product product);

    ProductsListDTO getAllProducts();

    ProductDTO getProductInfo(Long id);

    ProductsListDTO getProductsByCategory(String category);

    ProductsListDTO getProductsByShoeSize(int size);

    Optional<Product> getById(Long id);

    ProductsListDTO getProductsByBrand(String brand);

    ProductsListDTO getFilteredProducts(List<Integer> sizes, List<BrandName> brands, List<String> colors,
                                        BigDecimal minPrice, BigDecimal maxPrice);
}