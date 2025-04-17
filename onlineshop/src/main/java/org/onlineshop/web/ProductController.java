package org.onlineshop.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.ProductDTO;
import org.onlineshop.model.exportDTO.ProductsListDTO;
import org.onlineshop.model.importDTO.AddCartItemDTO;
import org.onlineshop.model.importDTO.AddProductDTO;
import org.onlineshop.service.interfaces.BrandService;
import org.onlineshop.service.interfaces.CategoryService;
import org.onlineshop.service.interfaces.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final BrandService brandService;
    private final CategoryService categoryService;
    private final ProductService productService;

    public ProductController(BrandService brandService, CategoryService categoryService, ProductService productService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
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
    public ModelAndView showAllProducts() {

        ModelAndView modelAndView = new ModelAndView("products");

        ProductsListDTO allProducts = this.productService.getAllProducts();

        modelAndView.addObject("products", allProducts);

        if (allProducts.getProducts().isEmpty()) {
            modelAndView.addObject("warningMessage", "Все още няма добавени продукти за разглеждане!");
        }

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

        if (productDTO == null) {
            redirectAttributes.addFlashAttribute("failureMessage",
                    "Продуктът, който се опитвате да достъпите, не съществува!");

            return new ModelAndView("redirect:/products/all");
        }

        modelAndView.addObject("product", productDTO);

        return modelAndView;
    }

    @PostMapping("/add-to-shopping-cart/{id}")
    public ModelAndView addProductToShoppingCart(@PathVariable("id") Long productId,
                                                 @Valid @ModelAttribute("addCartItemDTO") AddCartItemDTO addCartItemDTO,
                                                 BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                                 HttpSession session) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addCartItemDTO", addCartItemDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addCartItemDTO",
                            bindingResult);

            return new ModelAndView("redirect:/products/product/" + productId);
        }

        Result result = this.productService.addProductToShoppingCart(productId, addCartItemDTO, session);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/products/product/" + productId);
    }
}
