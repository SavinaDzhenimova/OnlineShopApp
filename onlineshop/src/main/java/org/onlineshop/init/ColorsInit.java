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

                        this.colorRepository.saveAndFlush(color);
                    });
        }
    }
}
