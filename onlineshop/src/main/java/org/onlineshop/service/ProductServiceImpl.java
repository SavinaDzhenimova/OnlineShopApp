package org.onlineshop.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.onlineshop.model.entity.*;
import org.onlineshop.model.enums.BrandName;
import org.onlineshop.model.enums.CategoryName;
import org.onlineshop.model.exportDTO.ProductDTO;
import org.onlineshop.model.importDTO.AddProductDTO;
import org.onlineshop.model.importDTO.QuantitySizeDTO;
import org.onlineshop.repository.ProductRepository;
import org.onlineshop.service.interfaces.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        productDTO.setSalePercent(product.getSalePercent());

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
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable).map(this::mapProductToDTO);
    }

    @Override
    public Page<ProductDTO> getFilteredProducts(List<Integer> sizes, List<BrandName> selectedBrands,
                                                List<String> selectedColors, BigDecimal minPrice, BigDecimal maxPrice,
                                                Pageable pageable) {

        Specification<Product> spec = Specification.where(null);

        if (selectedBrands != null && !selectedBrands.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    root.get("brand").get("brandName").in(selectedBrands));
        }

        if (selectedColors != null && !selectedColors.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    root.get("color").get("colorName").in(selectedColors));
        }

        if (minPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        }

        if (maxPrice != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("price"), maxPrice));
        }

        if (sizes != null && !sizes.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                Join<?, ?> sizesJoin = root.join("quantitySize", JoinType.INNER);
                return cb.and(
                        sizesJoin.get("size").get("size").in(sizes),
                        cb.greaterThan(sizesJoin.get("quantity"), 0)
                );
            });
        }

        Page<Product> result = this.productRepository.findAll(spec, pageable);

        return result.map(this::mapProductToDTO);
    }

    @Override
    public Page<ProductDTO> getProductsByCategory(String category, Pageable pageable) {

        CategoryName categoryName = switch (category) {
            case "men" -> CategoryName.MEN;
            case "women" -> CategoryName.WOMEN;
            case "children" -> CategoryName.CHILDREN;
            default -> throw new IllegalArgumentException("Невалидна категория: " + category);
        };

        return this.productRepository.findAllByCategoryName(categoryName, pageable)
                .map(this::mapProductToDTO);
    }

    @Override
    public Page<ProductDTO> getProductsByBrand(String brand, Pageable pageable) {

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

        return this.productRepository.findAllByBrandName(brandName, pageable)
                .map(this::mapProductToDTO);
    }

    @Override
    public Page<ProductDTO> getProductsByShoeSize(int size, Pageable pageable) {

        return this.productRepository.findAllBySizeWithStock(size, pageable)
                .map(this::mapProductToDTO);
    }

    @Override
    public Page<ProductDTO> getNewProducts(Pageable pageable) {

        return this.productRepository.findAllByIsNewTrue(pageable)
                .map(this::mapProductToDTO);
    }

    @Override
    public Page<ProductDTO> getProductsOnSale(Pageable pageable) {
        return this.productRepository.findAllByIsOnSaleTrue(pageable)
                .map(this::mapProductToDTO);
    }

    @Override
    public Optional<Product> getById(Long id) {
        return this.productRepository.findById(id);
    }

    @Override
    public List<Product> getAll() {
        return this.productRepository.findAll();
    }

    @Override
    public void saveAndFlush(Product product) {
        this.productRepository.saveAndFlush(product);
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
