package org.onlineshop.service;

import org.onlineshop.model.entity.*;
import org.onlineshop.model.enums.CategoryName;
import org.onlineshop.model.enums.Size;
import org.onlineshop.model.exportDTO.ProductDTO;
import org.onlineshop.model.exportDTO.ProductsListDTO;
import org.onlineshop.model.importDTO.AddProductDTO;
import org.onlineshop.repository.ProductRepository;
import org.onlineshop.service.interfaces.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final QuantitySizeService quantitySizeService;
    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ImageService imageService;

    public ProductServiceImpl(ProductRepository productRepository, QuantitySizeService quantitySizeService,
                              BrandService brandService, CategoryService categoryService, ImageService imageService) {
        this.productRepository = productRepository;
        this.quantitySizeService = quantitySizeService;
        this.brandService = brandService;
        this.categoryService = categoryService;
        this.imageService = imageService;
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

        try {
            List<String> imageUrls = this.imageService.saveImages(addProductDTO.getImages());

            List<Image> images = new ArrayList<>();

            for (String imageUrl : imageUrls) {
                Image image = new Image();

                image.setImageUrl(imageUrl);
                image.setProduct(product);
                images.add(image);
            }

            product.setImages(images);
        } catch (IOException e) {
            return null;
        }

        Set<QuantitySize> quantitySizes = addProductDTO.getSizes().stream()
                .map(quantitySizeDTO -> {
                    QuantitySize quantitySize = new QuantitySize();

                    quantitySize.setSize(quantitySizeDTO.getSize());
                    quantitySize.setQuantity(quantitySizeDTO.getQuantity());
                    quantitySize.setProduct(product);

                    return quantitySize;
                }).collect(Collectors.toSet());

        quantitySizes.forEach(this.quantitySizeService::saveAndFlush);

        product.setQuantitySize(quantitySizes);

        this.productRepository.saveAndFlush(product);

        return new Result(true, "Успешно добавихте продукт " + addProductDTO.getName());
    }

    @Override
    public ProductsListDTO getAllProducts() {

        List<Product> allProducts = this.productRepository.findAll();

        List<ProductDTO> productDTOS = allProducts.stream()
                .map(product -> {
                    ProductDTO productDTO = new ProductDTO();

                    productDTO.setId(product.getId());
                    productDTO.setName(product.getName());
                    productDTO.setDescription(product.getDescription());
                    productDTO.setPrice(product.getPrice());
                    productDTO.setBrand(product.getBrand().getBrandName());

                    List<String> imageUrls = this.mapImageToImageUrl(product.getImages());
                    productDTO.setImageUrls(imageUrls);

                    Set<String> categories = new LinkedHashSet<>(this.mapCategoriesToString(product.getCategories()));
                    productDTO.setCategories(categories);

                    Set<Integer> sizes = new TreeSet<>(this.mapQuantitySizeToIntegers(product.getQuantitySize()));
                    productDTO.setSizes(sizes);

                    return productDTO;
                }).toList();

        return new ProductsListDTO(productDTOS);
    }

    private List<String> mapImageToImageUrl(List<Image> images) {
        List<String> imageUrls = new ArrayList<>();

        for (Image image : images) {
            if (image.getImageUrl() != null) {
                imageUrls.add(image.getImageUrl());
            }
        }

        return imageUrls;
    }

    private Set<Integer> mapQuantitySizeToIntegers(Set<QuantitySize> quantitySizes) {
        Set<Integer> integerSizes = new HashSet<>();

        for (QuantitySize quantitySize : quantitySizes) {
            if (quantitySize != null && quantitySize.getSize() != null) {
                integerSizes.add(quantitySize.getSize().getValue());
            }
        }

        return integerSizes;
    }

    private Set<String> mapCategoriesToString(Set<Category> categories) {
        Set<String> stringCategories = new HashSet<>();

        for (Category category : categories) {
            if (category == null || category.getCategoryName() == null) continue;

            switch (category.getCategoryName()) {
                case MEN -> stringCategories.add("Мъжки обувки");
                case WOMEN -> stringCategories.add("Дамски обувки");
                case CHILDREN -> stringCategories.add("Детски обувки");
                case NEW -> stringCategories.add("Ново");
                case SALE -> stringCategories.add("Разпродажба");
            }
        }

        return stringCategories;
    }
}
