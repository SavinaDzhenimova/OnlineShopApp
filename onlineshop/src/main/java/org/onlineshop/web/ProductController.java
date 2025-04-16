package org.onlineshop.web;

import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.enums.Size;
import org.onlineshop.model.importDTO.AddProductDTO;
import org.onlineshop.service.interfaces.BrandService;
import org.onlineshop.service.interfaces.CategoryService;
import org.onlineshop.service.interfaces.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ModelAndView registerUser(@Valid @ModelAttribute("addProductDTO") AddProductDTO addProductDTO,
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
    public ModelAndView showAllProducts(Model model) {

        if (!model.containsAttribute("addProductDTO")) {
            model.addAttribute("addProductDTO", new AddProductDTO());
        }

        model.addAttribute("brands", this.brandService.getAllBrands());
        model.addAttribute("categoryIds", this.categoryService.getAllCategories());
        model.addAttribute("shoeSizes", Size.values());

        return new ModelAndView("products");
    }
}
