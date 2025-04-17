package org.onlineshop.service;

import org.onlineshop.model.entity.Category;
import org.onlineshop.repository.CategoryRepository;
import org.onlineshop.service.interfaces.CategoryService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Set<String> mapCategoriesToString(Set<Category> categories) {
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

    @Override
    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return this.categoryRepository.findById(id);
    }
}
