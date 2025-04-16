package org.onlineshop.model.exportDTO;

import java.util.List;

public class ProductsListDTO {

    private List<ProductDTO> products;

    public ProductsListDTO(List<ProductDTO> products) {
        this.products = products;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
