package org.onlineshop.init;

import org.onlineshop.model.entity.Category;
import org.onlineshop.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CategoriesInit implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoriesInit(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        if (this.categoryRepository.count() == 0) {

            Arrays.stream(org.onlineshop.model.enums.CategoryName.values())
                    .forEach(categoryName -> {
                        Category category = new Category();
                        category.setCategoryName(categoryName);

                        String description = switch (categoryName) {
                            case MEN -> "Мъжки спортни обувки";
                            case WOMEN -> "Дамски спортни обувки";
                            case CHILDREN -> "Детски спортни обувки";
                            case NEW -> "Нови спортни обувки";
                            case SALE -> "Спортни обувки на разпродажба";
                        };

                        category.setDescription(description);
                        this.categoryRepository.saveAndFlush(category);
                    });
        }
    }
}
