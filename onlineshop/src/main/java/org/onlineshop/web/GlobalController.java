package org.onlineshop.web;

import org.onlineshop.model.entity.Brand;
import org.onlineshop.model.entity.Category;
import org.onlineshop.model.entity.Color;
import org.onlineshop.model.entity.ShoeSize;
import org.onlineshop.model.enums.SizeName;
import org.onlineshop.service.interfaces.BrandService;
import org.onlineshop.service.interfaces.CategoryService;
import org.onlineshop.service.interfaces.ColorService;
import org.onlineshop.service.interfaces.ShoeSizeService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalController {

    private final BrandService brandService;
    private final ColorService colorService;
    private final CategoryService categoryService;
    private final ShoeSizeService shoeSizeService;

    public GlobalController(BrandService brandService, ColorService colorService, CategoryService categoryService,
                            ShoeSizeService shoeSizeService) {
        this.brandService = brandService;
        this.colorService = colorService;
        this.categoryService = categoryService;
        this.shoeSizeService = shoeSizeService;
    }

    @ModelAttribute("brands")
    public List<Brand> getBrands() {
        return this.brandService.getAllBrands();
    }

    @ModelAttribute("colors")
    public List<Color> getColors() {
        return this.colorService.getAllColors();
    }

    @ModelAttribute("categoryIds")
    public List<Category> getCategories() {
        return this.categoryService.getAllCategories();
    }

    @ModelAttribute("shoeSizes")
    public List<ShoeSize> shoeSizes() {
        return this.shoeSizeService.getAllShoeSizes();
    }
}