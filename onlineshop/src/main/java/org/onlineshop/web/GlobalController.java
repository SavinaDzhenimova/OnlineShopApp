package org.onlineshop.web;

import org.onlineshop.model.entity.Brand;
import org.onlineshop.model.entity.Category;
import org.onlineshop.model.enums.Size;
import org.onlineshop.service.interfaces.BrandService;
import org.onlineshop.service.interfaces.CategoryService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalController {

    private final BrandService brandService;
    private final CategoryService categoryService;

    public GlobalController(BrandService brandService, CategoryService categoryService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("brands")
    public List<Brand> getBrands() {
        return this.brandService.getAllBrands();
    }

    @ModelAttribute("categoryIds")
    public List<Category> getCategories() {
        return this.categoryService.getAllCategories();
    }

    @ModelAttribute("shoeSizes")
    public Size[] shoeSizes() {
        return Size.values();
    }
}