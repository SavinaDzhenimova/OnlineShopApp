package org.onlineshop.service;

import org.onlineshop.model.entity.*;
import org.onlineshop.model.importDTO.AddProductDTO;
import org.onlineshop.repository.ProductRepository;
import org.onlineshop.service.interfaces.BrandService;
import org.onlineshop.service.interfaces.CategoryService;
import org.onlineshop.service.interfaces.ProductService;
import org.onlineshop.service.interfaces.QuantitySizeService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final QuantitySizeService quantitySizeService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, QuantitySizeService quantitySizeService,
                              BrandService brandService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.quantitySizeService = quantitySizeService;
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @Override
    public Result addProduct(AddProductDTO addProductDTO) {

        Optional<Brand> optionalBrand = this.brandService.getBrandById(addProductDTO.getBrandId());

        if (optionalBrand.isEmpty()) {
            return new Result(false, "Марката, която искате да добавите на този продукт, не съществува!");
        }

        Set<Category> categories = new HashSet<>();
        for (Long categoryId : addProductDTO.getCategories()) {
            Optional<Category> optionalCategory = categoryService.getCategoryById(categoryId);

            if (optionalCategory.isEmpty()) {
                return new Result(false,
                        "Категорията, която искате да добавите на този продукт, не съществува!");
            }
            categories.add(optionalCategory.get());
        }

        Product product = new Product();

        product.setName(addProductDTO.getName());
        product.setDescription(addProductDTO.getDescription());
        product.setPrice(addProductDTO.getPrice());
        product.setBrand(optionalBrand.get());
        product.setCategories(categories);

        this.productRepository.saveAndFlush(product);

        Set<QuantitySize> quantitySizes = addProductDTO.getSizes().stream()
                .map(quantitySizeDTO -> {
                    QuantitySize quantitySize = new QuantitySize();

                    quantitySize.setSize(quantitySizeDTO.getSize());
                    quantitySize.setQuantity(quantitySize.getQuantity());
                    quantitySize.setProduct(product);

                    return quantitySize;
                }).collect(Collectors.toSet());

        quantitySizes.forEach(this.quantitySizeService::saveAndFlush);

        product.setQuantitySize(quantitySizes);

        this.productRepository.saveAndFlush(product);

        return new Result(true, "Успешно добавихте продукт " + addProductDTO.getName());
    }
}
