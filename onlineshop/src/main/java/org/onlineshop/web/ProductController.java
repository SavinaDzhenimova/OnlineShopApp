package org.onlineshop.web;

import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.enums.BrandName;
import org.onlineshop.model.exportDTO.ProductDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;
import org.onlineshop.model.importDTO.AddProductDTO;
import org.onlineshop.service.interfaces.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/add-product")
    public ModelAndView showAddProductForm(Model model) {

        if (!model.containsAttribute("addProductDTO")) {
            model.addAttribute("addProductDTO", new AddProductDTO());
        }

        return new ModelAndView("add-product");
    }

    @PostMapping("/add-product")
    public ModelAndView addProduct(@Valid @ModelAttribute("addProductDTO") AddProductDTO addProductDTO,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addProductDTO", addProductDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addProductDTO",
                            bindingResult);

            return new ModelAndView("add-product");
        }

        Result result = this.productService.addProduct(addProductDTO);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());

            return new ModelAndView("redirect:/products/add-product");
        }

        return new ModelAndView("redirect:/products/all");
    }

    @GetMapping("/all")
    public ModelAndView showFilteredOrAllProducts(@RequestParam(required = false) List<Integer> sizes,
                                                  @RequestParam(required = false) List<BrandName> selectedBrands,
                                                  @RequestParam(required = false) List<String> selectedColors,
                                                  @RequestParam(required = false) BigDecimal minPrice,
                                                  @RequestParam(required = false) BigDecimal maxPrice,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "12") int size) {

        ModelAndView modelAndView = new ModelAndView("products");

        Page<ProductDTO> pagedProducts;
        String warningMessage = "";

        boolean hasFilters = (sizes != null && !sizes.isEmpty()) ||
                (selectedBrands != null && !selectedBrands.isEmpty()) ||
                (selectedColors != null && !selectedColors.isEmpty()) ||
                minPrice != null || maxPrice != null;

        Pageable pageable = PageRequest.of(page, size);

        if (hasFilters) {
            pagedProducts = productService.getFilteredProducts(sizes, selectedBrands, selectedColors, minPrice, maxPrice, pageable);
            if (pagedProducts.isEmpty()) {
                warningMessage = "Няма намерени продукти по избраните критерии!";
            }
        } else {
            pagedProducts = productService.getAllProducts(pageable);
            if (pagedProducts.isEmpty()) {
                warningMessage = "Все още няма добавени продукти за разглеждане!";
            }
        }

        modelAndView.addObject("sizes", sizes);
        modelAndView.addObject("selectedBrands", selectedBrands);
        modelAndView.addObject("selectedColors", selectedColors);
        modelAndView.addObject("minPrice", minPrice);
        modelAndView.addObject("maxPrice", maxPrice);

        modelAndView.addObject("products", pagedProducts);
        modelAndView.addObject("title", "Спортни обувки");

        if (pagedProducts.isEmpty()) {
            modelAndView.addObject("warningMessage", warningMessage);
        }

        return modelAndView;
    }

    @GetMapping("/category/{category}")
    public ModelAndView showProductsByCategory(@PathVariable("category") String category,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "12") int size) {

        ModelAndView modelAndView = new ModelAndView("products");

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductDTO> productsByCategory = this.productService.getProductsByCategory(category, pageable);

        modelAndView.addObject("products", productsByCategory);

        if (productsByCategory.isEmpty()) {
            modelAndView.addObject("warningMessage",
                    "Все още няма добавени продукти за разглеждане в тази категория!");
        }

        String categoryTitle = switch (category) {
            case "women" -> "Дамски обувки";
            case "men" -> "Мъжки обувки";
            case "children" -> "Детски обувки";
            default -> "Спортни обувки";
        };

        modelAndView.addObject("title", categoryTitle);

        return modelAndView;
    }

    @GetMapping("/brand/{brand}")
    public ModelAndView showProductsByBrand(@PathVariable("brand") String brand,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "12") int size) {

        ModelAndView modelAndView = new ModelAndView("products");

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductDTO> productsByproductsByBrand = this.productService.getProductsByBrand(brand, pageable);

        modelAndView.addObject("products", productsByproductsByBrand);

        if (productsByproductsByBrand.isEmpty()) {
            modelAndView.addObject("warningMessage",
                    "Все още няма добавени продукти за разглеждане от тази марка!");
        }

        String brandTitle = switch (brand) {
            case "nike" -> "Nike обувки";
            case "adidas" -> "Adidas обувки";
            case "puma" -> "Puma обувки";
            case "guess" -> "Guess обувки";
            case "salomon" -> "Solomon обувки";
            case "skechers" -> "Skechers обувки";
            case "reebok" -> "Reebok обувки";
            case "new-balance" -> "New Balance обувки";
            case "calvin-klein" -> "Calvin Klein обувки";
            case "champion" -> "Champion обувки";
            case "asics" -> "Asics обувки";
            case "tommy-hilfiger" -> "Tommy Hilfiger обувки";
            case "timberland" -> "Timberland обувки";
            case "lacoste" -> "Lacoste обувки";
            case "converse" -> "Converse обувки";
            case "the-north-face" -> "The North Face обувки";
            case "napapijri" -> "Napapijri обувки";
            case "us-polo-assn" -> "US Polo Assn обувки";
            case "columbia" -> "Columbia обувки";
            case "caterpillar" -> "Caterpillar обувки";
            case "diadora" -> "Diadora обувки";
            case "fila" -> "FILA обувки";
            case "kappa" -> "KAPPA обувки";
            case "crocks" -> "Crocks обувки";
            case "palladium" -> "Palladium обувки";
            case "reef" -> "REEF обувки";
            case "rip-curl" -> "Rip Curl обувки";
            case "lotto" -> "Lotto обувки";
            case "under-armor" -> "Under Armor обувки";
            default -> "Всички обувки";
        };

        modelAndView.addObject("title", brandTitle);

        return modelAndView;
    }

    @GetMapping("/size/{size}")
    public ModelAndView showProductsBySize(@PathVariable("size") int shoeSize,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "12") int size) {

        ModelAndView modelAndView = new ModelAndView("products");

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductDTO> productsByproductsByShoeSize = this.productService.getProductsByShoeSize(shoeSize, pageable);

        modelAndView.addObject("products", productsByproductsByShoeSize);

        if (productsByproductsByShoeSize.isEmpty()) {
            modelAndView.addObject("warningMessage",
                    "Все още няма добавени продукти за разглеждане с този размер!");
        }

        modelAndView.addObject("title", "Спортни обувки №" + shoeSize);

        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView showNewProducts(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "12") int size) {

        ModelAndView modelAndView = new ModelAndView("products");

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductDTO> newProducts = this.productService.getNewProducts(pageable);

        modelAndView.addObject("products", newProducts);

        if (newProducts.isEmpty()) {
            modelAndView.addObject("warningMessage",
                    "Все още няма добавени продукти за разглеждане в тази категория!");
        }

        modelAndView.addObject("title", "Нови модели спортни обувки");

        return modelAndView;
    }

    @GetMapping("/on-sale")
    public ModelAndView showProductsOnSale(@RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "12") int size) {

        ModelAndView modelAndView = new ModelAndView("products");

        Pageable pageable = PageRequest.of(page, size);

        Page<ProductDTO> productsOnSale = this.productService.getProductsOnSale(pageable);

        modelAndView.addObject("products", productsOnSale);

        if (productsOnSale.isEmpty()) {
            modelAndView.addObject("warningMessage",
                    "Все още няма добавени продукти за разглеждане в тази категория!");
        }

        modelAndView.addObject("title", "Разпродажба на спортни обувки");

        return modelAndView;
    }

    @GetMapping("/product/{id}")
    public ModelAndView showProductInfo(@PathVariable("id") Long id, Model model) {

        if (!model.containsAttribute("addCartItemDTO")) {
            model.addAttribute("addCartItemDTO", new AddCartItemDTO());
        }

        ModelAndView modelAndView = new ModelAndView("product-details");

        ProductDTO productDTO = this.productService.getProductInfo(id);

        modelAndView.addObject("product", productDTO);

        return modelAndView;
    }
}