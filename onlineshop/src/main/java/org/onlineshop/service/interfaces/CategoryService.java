package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> getAllCategories();
    Optional<Category> getCategoryById(Long id);
}
