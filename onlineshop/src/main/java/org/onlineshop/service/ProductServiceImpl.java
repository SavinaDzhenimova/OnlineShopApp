package org.onlineshop.service;

import org.onlineshop.model.entity.*;
import org.onlineshop.model.enums.CategoryName;
import org.onlineshop.model.exportDTO.ProductDTO;
import org.onlineshop.model.exportDTO.ProductsListDTO;
import org.onlineshop.model.importDTO.AddProductDTO;
import org.onlineshop.model.importDTO.QuantitySizeDTO;
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
    private final ColorService colorService;
    private final CategoryService categoryService;
    private final ShoeSizeService shoeSizeService;
    private final ImageService imageService;

    public ProductServiceImpl(ProductRepository productRepository, QuantitySizeService quantitySizeService,
                              BrandService brandService, ColorService colorService, CategoryService categoryService,
                              ShoeSizeService shoeSizeService, ImageService imageService) {
        this.productRepository = productRepository;
        this.quantitySizeService = quantitySizeService;
        this.brandService = brandService;
        this.colorService = colorService;
        this.categoryService = categoryService;
        this.shoeSizeService = shoeSizeService;
        this.imageService = imageService;
    }

    @Override
    public Result addProduct(AddProductDTO addProductDTO) {

        Optional<Brand> optionalBrand = this.brandService.getBrandById(addProductDTO.getBrandId());

        if (optionalBrand.isEmpty()) {
            return new Result(false, "Марката, която искате да добавите на този продукт, не съществува!");
        }

        Optional<Color> optionalColor = this.colorService.findColorById(addProductDTO.getColorId());

        if (optionalColor.isEmpty()) {
            return new Result(false, "Цветът, който искате да добавите на този продукт, не съществува!");
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
        product.setColor(optionalColor.get());
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

                    Optional<ShoeSize> optionalSize = this.shoeSizeService.getBySize(quantitySizeDTO.getSize());

                    quantitySize.setSize(optionalSize.get());
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
    public ProductDTO getProductInfo(Long id) {

        Optional<Product> optionalProduct = this.productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            return null;
        }

        Product product = optionalProduct.get();

        return this.mapProductToDTO(product);
    }

    @Override
    public ProductDTO mapProductToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setBrand(product.getBrand().getBrandName());
        productDTO.setBrandUrl(product.getBrand().getBrandUrl());
        productDTO.setColor(product.getColor().getDescription());

        List<String> imageUrls = this.mapImageToImageUrl(product.getImages());
        productDTO.setImageUrls(imageUrls);

        Set<String> categories = new LinkedHashSet<>(this.categoryService.mapCategoriesToString(product.getCategories()));
        productDTO.setCategories(categories);

        Set<QuantitySizeDTO> sizes = new TreeSet<>(this.mapQuantitySizeToDTO(product.getQuantitySize()));
        productDTO.setSizes(sizes);

        return productDTO;
    }

    @Override
    public ProductsListDTO getAllProducts() {

        List<Product> allProducts = this.productRepository.findAll();

        List<ProductDTO> productDTOS = allProducts.stream().map(this::mapProductToDTO).toList();

        return new ProductsListDTO(productDTOS);
    }

    @Override
    public ProductsListDTO getProductsByCategory(String category) {

        CategoryName categoryName = switch (category) {
            case "men" -> CategoryName.MEN;
            case "women" -> CategoryName.WOMEN;
            case "children" -> CategoryName.CHILDREN;
            case "new" -> CategoryName.NEW;
            case "sale" -> CategoryName.SALE;
            default -> throw new IllegalArgumentException("Невалидна категория: " + category);
        };

        List<ProductDTO> productDTOList = this.productRepository
                .findAllByCategoryName(categoryName).stream()
                .map(this::mapProductToDTO)
                .toList();

        return new ProductsListDTO(productDTOList);
    }

    @Override
    public Optional<Product> getById(Long id) {
        return this.productRepository.findById(id);
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

    private Set<QuantitySizeDTO> mapQuantitySizeToDTO(Set<QuantitySize> quantitySizes) {
        Set<QuantitySizeDTO> quantitySizeDTOS = new HashSet<>();

        for (QuantitySize quantitySize : quantitySizes) {
            if (quantitySize != null && quantitySize.getSize() != null) {
                QuantitySizeDTO quantitySizeDTO = new QuantitySizeDTO();

                quantitySizeDTO.setSize(quantitySize.getSize().getSize());
                quantitySizeDTO.setQuantity(quantitySize.getQuantity());

                quantitySizeDTOS.add(quantitySizeDTO);
            }
        }

        return quantitySizeDTOS;
    }
}
