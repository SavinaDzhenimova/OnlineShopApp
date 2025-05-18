package org.onlineshop.service;

import org.onlineshop.model.entity.*;
import org.onlineshop.model.enums.BrandName;
import org.onlineshop.model.enums.CategoryName;
import org.onlineshop.model.exportDTO.ProductDTO;
import org.onlineshop.model.exportDTO.ProductsListDTO;
import org.onlineshop.model.importDTO.AddProductDTO;
import org.onlineshop.model.importDTO.QuantitySizeDTO;
import org.onlineshop.repository.ProductRepository;
import org.onlineshop.service.interfaces.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
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

        Optional<Category> optionalCategory = categoryService.getCategoryById(addProductDTO.getCategoryId());
        if (optionalCategory.isEmpty()) {
            return new Result(false, "Категорията, която искате да добавите на този продукт, не съществува!");
        }

        Product product = new Product();

        product.setName(addProductDTO.getName());
        product.setDescription(addProductDTO.getDescription());
        product.setPrice(addProductDTO.getPrice());
        product.setBrand(optionalBrand.get());
        product.setColor(optionalColor.get());
        product.setCategory(optionalCategory.get());
        product.setAddedOn(LocalDate.now());
        product.setNew(true);
        product.setOnSale(false);

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
            throw new NoSuchElementException("Този продукт не съществува!");
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
        productDTO.setOldPrice(product.getOldPrice());
        productDTO.setBrand(product.getBrand().getBrandName());
        productDTO.setBrandUrl(product.getBrand().getBrandUrl());
        productDTO.setColor(product.getColor().getColorName().getDisplayName());
        productDTO.setCategory(product.getCategory().getCategoryName().getDisplayName());
        productDTO.setNew(product.isNew());
        productDTO.setOnSale(product.isOnSale());

        List<String> imageUrls = this.mapImageToImageUrl(product.getImages());
        productDTO.setImageUrls(imageUrls);

        Set<QuantitySize> filteredQuantitySizes = product.getQuantitySize().stream()
                .filter(quantitySize -> quantitySize.getQuantity() > 0)
                .collect(Collectors.toSet());
        Set<QuantitySizeDTO> sizes = new TreeSet<>(this.mapQuantitySizeToDTO(filteredQuantitySizes));
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
    public ProductsListDTO getFilteredProducts(List<Integer> sizes, List<BrandName> brands, List<String> colors,
                                               BigDecimal minPrice, BigDecimal maxPrice) {

        List<ProductDTO> productDTOS = productRepository.findAll().stream()
                .filter(product -> sizes == null || product.getQuantitySize().stream()
                        .anyMatch(quantitySize ->
                                sizes.contains(quantitySize.getSize().getSize()) && quantitySize.getQuantity() > 0))
                .filter(product -> brands == null || brands.contains(product.getBrand().getBrandName()))
                .filter(product -> colors == null || colors.contains(product.getColor().getColorName().getDisplayName()))
                .filter(product -> minPrice == null || product.getPrice().compareTo(minPrice) >= 0)
                .filter(product -> maxPrice == null || product.getPrice().compareTo(maxPrice) <= 0)
                .map(this::mapProductToDTO)
                .toList();

        return new ProductsListDTO(productDTOS);
    }

    @Override
    public ProductsListDTO getProductsByCategory(String category) {

        CategoryName categoryName = switch (category) {
            case "men" -> CategoryName.MEN;
            case "women" -> CategoryName.WOMEN;
            case "children" -> CategoryName.CHILDREN;
            default -> throw new IllegalArgumentException("Невалидна категория: " + category);
        };

        List<ProductDTO> productDTOList = this.productRepository
                .findAllByCategoryName(categoryName).stream()
                .map(this::mapProductToDTO)
                .toList();

        return new ProductsListDTO(productDTOList);
    }

    @Override
    public ProductsListDTO getProductsByBrand(String brand) {

        BrandName brandName = switch (brand) {
            case "nike" -> BrandName.NIKE;
            case "adidas" -> BrandName.ADIDAS;
            case "puma" -> BrandName.PUMA;
            case "guess" -> BrandName.GUESS;
            case "skechers" -> BrandName.SKECHERS;
            case "salomon" -> BrandName.SALOMON;
            case "reebok" -> BrandName.REEBOK;
            case "new-balance" -> BrandName.NEW_BALANCE;
            case "calvin-klein" -> BrandName.CALVIN_KLEIN;
            case "champion" -> BrandName.CHAMPION;
            case "asics" -> BrandName.ASICS;
            case "tommy-hilfiger" -> BrandName.TOMMY_HILFIGER;
            case "timberland" -> BrandName.TIMBERLAND;
            case "lacoste" -> BrandName.LACOSTE;
            case "converse" -> BrandName.CONVERSE;
            case "the-north-face" -> BrandName.THE_NORTH_FACE;
            case "napapijri" -> BrandName.NAPAPIJRI;
            case "us-polo-assn" -> BrandName.US_POLO_ASSN;
            case "columbia" -> BrandName.COLUMBIA;
            case "caterpillar" -> BrandName.CATERPILLAR;
            case "diadora" -> BrandName.DIADORA;
            case "fila" -> BrandName.FILA;
            case "kappa" -> BrandName.KAPPA;
            case "crocks" -> BrandName.CROCKS;
            case "palladium" -> BrandName.PALLADIUM;
            case "reef" -> BrandName.REEF;
            case "rip-curl" -> BrandName.RIP_CURL;
            case "lotto" -> BrandName.LOTTO;
            case "under-armor" -> BrandName.UNDER_ARMOR;
            default -> throw new IllegalArgumentException("Невалидна марка: " + brand);
        };

        List<ProductDTO> productDTOList = this.productRepository
                .findAllByBrandName(brandName).stream()
                .map(this::mapProductToDTO)
                .toList();

        return new ProductsListDTO(productDTOList);
    }

    @Override
    public ProductsListDTO getProductsByShoeSize(int size) {
        
        List<ProductDTO> productDTOList = this.productRepository
                .findAllBySizeWithStock(size).stream()
                .map(this::mapProductToDTO)
                .toList();

        return new ProductsListDTO(productDTOList);
    }

    @Override
    public ProductsListDTO getNewProducts() {

        List<ProductDTO> productDTOList = this.productRepository.findAllByIsNewTrue().stream()
                .map(this::mapProductToDTO)
                .toList();

        return new ProductsListDTO(productDTOList);
    }

    @Override
    public ProductsListDTO getProductsOnSale() {

        List<ProductDTO> productDTOList = this.productRepository.findAllByIsOnSaleTrue().stream()
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
