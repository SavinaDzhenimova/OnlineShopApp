package org.onlineshop.service;

import org.onlineshop.model.entity.*;
import org.onlineshop.model.exportDTO.ProductDTO;
import org.onlineshop.model.exportDTO.ProductsListDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;
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
    private final ColorService colorService;
    private final CategoryService categoryService;
    private final ShoeSizeService shoeSizeService;
    private final ImageService imageService;
    private final UserService userService;
    private final CartItemService cartItemService;

    public ProductServiceImpl(ProductRepository productRepository, QuantitySizeService quantitySizeService,
                              BrandService brandService, ColorService colorService, CategoryService categoryService,
                              ShoeSizeService shoeSizeService, ImageService imageService, UserService userService,
                              CartItemService cartItemService) {
        this.productRepository = productRepository;
        this.quantitySizeService = quantitySizeService;
        this.brandService = brandService;
        this.colorService = colorService;
        this.categoryService = categoryService;
        this.shoeSizeService = shoeSizeService;
        this.imageService = imageService;
        this.userService = userService;
        this.cartItemService = cartItemService;
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
    public Result addProductToShoppingCart(AddCartItemDTO addCartItemDTO) {

        Optional<Product> optionalProduct = this.productRepository.findById(addCartItemDTO.getProductId());

        if (optionalProduct.isEmpty()) {
            return new Result(false, "Продуктът, който се опитвате да добавите в количката, не съществува!");
        }

        Product product = optionalProduct.get();

        Optional<QuantitySize> matchingSize = product.getQuantitySize()
                .stream()
                .filter(qs -> qs.getSize().getSize().equals(addCartItemDTO.getSize()))
                .findFirst();

        if (matchingSize.isEmpty()) {
            return new Result(false, "Избраният размер не е наличен за този продукт!");
        }

        int availableQuantity = matchingSize.get().getQuantity();
        int requestedQuantity = addCartItemDTO.getQuantity();

        if (availableQuantity < requestedQuantity) {
            return new Result(false, "Наличното количество за размер " +
                    addCartItemDTO.getSize() + " е само " + availableQuantity + " броя.");
        }

        CartItem cartItem = new CartItem();

        User loggedUser = this.userService.getLoggedUser();
        ShoppingCart loggedUserShoppingCart = loggedUser.getShoppingCart();

        cartItem.setShoppingCart(loggedUserShoppingCart);
        cartItem.setProduct(product);
        cartItem.setQuantity(addCartItemDTO.getQuantity());
        cartItem.setSize(addCartItemDTO.getSize());

        this.cartItemService.saveAndFlush(cartItem);

        return new Result(true, "Успешно добавихте този продукт във вашата количка!");
    }

    private ProductDTO mapProductToDTO(Product product) {
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

        Set<String> categories = new LinkedHashSet<>(this.mapCategoriesToString(product.getCategories()));
        productDTO.setCategories(categories);

        Set<Integer> sizes = new TreeSet<>(this.mapQuantitySizeToIntegers(product.getQuantitySize()));
        productDTO.setSizes(sizes);

        return productDTO;
    }

    @Override
    public ProductsListDTO getAllProducts() {

        List<Product> allProducts = this.productRepository.findAll();

        List<ProductDTO> productDTOS = allProducts.stream().map(this::mapProductToDTO).toList();

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
                integerSizes.add(quantitySize.getSize().getSize());
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
