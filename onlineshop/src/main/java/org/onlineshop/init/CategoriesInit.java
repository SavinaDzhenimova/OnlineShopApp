package org.onlineshop.init;

import org.onlineshop.model.entity.Category;
import org.onlineshop.model.entity.Role;
import org.onlineshop.model.enums.CategoryName;
import org.onlineshop.model.enums.RoleName;
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

            Arrays.stream(CategoryName.values())
                    .forEach(categoryName -> {
                        Category category = new Category();
                        category.setCategoryName(categoryName);

                        String description = switch (categoryName) {
                            case MALE -> "Мъжки спортни обувки";
                            case FEMALE -> "Дамски спортни обувки";
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
