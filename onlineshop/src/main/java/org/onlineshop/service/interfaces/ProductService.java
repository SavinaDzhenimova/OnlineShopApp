package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.ProductDTO;
import org.onlineshop.model.exportDTO.ProductsListDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;
import org.onlineshop.model.importDTO.AddProductDTO;

public interface ProductService {

    Result addProduct(AddProductDTO addProductDTO);

    ProductsListDTO getAllProducts();

    ProductDTO getProductInfo(Long id);

    Result addProductToShoppingCart(AddCartItemDTO addCartItemDTO);
}
