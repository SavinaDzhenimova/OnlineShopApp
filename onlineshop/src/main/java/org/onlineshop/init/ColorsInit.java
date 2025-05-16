package org.onlineshop.init;

import org.onlineshop.model.entity.Color;
import org.onlineshop.model.enums.ColorName;
import org.onlineshop.repository.ColorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ColorsInit implements CommandLineRunner {

    private final ColorRepository colorRepository;

    public ColorsInit(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public void run(String... args) {

        if (this.colorRepository.count() == 0) {

            Arrays.stream(ColorName.values())
                    .forEach(colorName -> {
                        Color color = new Color();
                        color.setColorName(colorName);

                        String description = "";

                        switch (colorName) {
                            case BLACK -> description = "Черен";
                            case WHITE -> description = "Бял";
                            case RED -> description = "Червен";
                            case BLUE -> description = "Син";
                            case GREEN -> description = "Зелен";
                            case GREY -> description = "Сив";
                            case BROWN -> description = "Кафяв";
                            case PINK -> description = "Розов";
                            case YELLOW -> description = "Жълт";
                            case ORANGE -> description = "Оранжев";
                            case BEIGE -> description = "Бежов";
                            case GOLD -> description = "Златен";
                            case CORAL -> description = "Корал";
                            case BORDEAUX -> description = "Бордо";
                            case BRONZE -> description = "Бронзов";
                            case SILVER -> description = "Сребрист";
                            case CAMOUFLAGE -> description = "Камуфлаж";
                            case LEOPARD -> description = "Леопардов";
                            case MULTICOLOR -> description = "Многоцветен";
                        }

                        color.setDescription(description);

                        this.colorRepository.saveAndFlush(color);
                    });
        }
    }
}
