package org.onlineshop.web;

import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.enums.BrandName;
import org.onlineshop.model.exportDTO.ProductDTO;
import org.onlineshop.model.exportDTO.ProductsListDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;
import org.onlineshop.model.importDTO.AddProductDTO;
import org.onlineshop.service.interfaces.ProductService;
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
                                                  @RequestParam(required = false) List<BrandName> brands,
                                                  @RequestParam(required = false) List<String> colors,
                                                  @RequestParam(required = false) BigDecimal minPrice,
                                                  @RequestParam(required = false) BigDecimal maxPrice) {

        ModelAndView modelAndView = new ModelAndView("products");
        ProductsListDTO products;
        String warningMessage = "";

        boolean hasFilters = (sizes != null && !sizes.isEmpty()) ||
                (brands != null && !brands.isEmpty()) ||
                (colors != null && !colors.isEmpty()) ||
                minPrice != null || maxPrice != null;

        if (hasFilters) {
            products = productService.getFilteredProducts(sizes, brands, colors, minPrice, maxPrice);

            if (products.getProducts().isEmpty()) {
                warningMessage = "Няма намерени продукти по избраните критерии!";
            }
        } else {
            products = productService.getAllProducts();
            warningMessage = "Все още няма добавени продукти за разглеждане!";
        }

        modelAndView.addObject("products", products);
        modelAndView.addObject("title", "Спортни обувки");

        if (products.getProducts().isEmpty()) {
            modelAndView.addObject("warningMessage", warningMessage);
        }

        return modelAndView;
    }

    @GetMapping("/category/{category}")
    public ModelAndView showProductsByCategory(@PathVariable("category") String category) {

        ModelAndView modelAndView = new ModelAndView("products");

        ProductsListDTO productsByCategory = this.productService.getProductsByCategory(category);

        modelAndView.addObject("products", productsByCategory);

        if (productsByCategory.getProducts().isEmpty()) {
            modelAndView.addObject("warningMessage",
                    "Все още няма добавени продукти за разглеждане в тази категория!");
        }

        String categoryTitle = switch (category) {
            case "women" -> "Дамски обувки";
            case "men" -> "Мъжки обувки";
            case "children" -> "Детски обувки";
            case "sale" -> "Разпродажба на обувки";
            case "new" -> "Нови модели обувки";
            default -> "Спортни обувки";
        };

        modelAndView.addObject("title", categoryTitle);

        return modelAndView;
    }

    @GetMapping("/brand/{brand}")
    public ModelAndView showProductsByBrand(@PathVariable("brand") String brand) {

        ModelAndView modelAndView = new ModelAndView("products");

        ProductsListDTO productsByproductsByBrand = this.productService.getProductsByBrand(brand);

        modelAndView.addObject("products", productsByproductsByBrand);

        if (productsByproductsByBrand.getProducts().isEmpty()) {
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
    public ModelAndView showProductsBySize(@PathVariable("size") int size) {

        ModelAndView modelAndView = new ModelAndView("products");

        ProductsListDTO productsByproductsByShoeSize = this.productService.getProductsByShoeSize(size);

        modelAndView.addObject("products", productsByproductsByShoeSize);

        if (productsByproductsByShoeSize.getProducts().isEmpty()) {
            modelAndView.addObject("warningMessage",
                    "Все още няма добавени продукти за разглеждане с този размер!");
        }

        modelAndView.addObject("title", "Спортни обувки №" + size);

        return modelAndView;
    }

    @GetMapping("/product/{id}")
    public ModelAndView showProductInfo(@PathVariable("id") Long id, Model model,
                                        RedirectAttributes redirectAttributes) {

        if (!model.containsAttribute("addCartItemDTO")) {
            model.addAttribute("addCartItemDTO", new AddCartItemDTO());
        }

        ModelAndView modelAndView = new ModelAndView("product-details");

        ProductDTO productDTO = this.productService.getProductInfo(id);

        modelAndView.addObject("product", productDTO);

        return modelAndView;
    }
}
