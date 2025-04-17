package org.onlineshop.init;

import org.onlineshop.model.entity.ShoeSize;
import org.onlineshop.model.enums.SizeName;
import org.onlineshop.repository.ShoeSizeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ShoeSizesInit implements CommandLineRunner {

    private final ShoeSizeRepository shoeSizeRepository;

    public ShoeSizesInit(ShoeSizeRepository shoeSizeRepository) {
        this.shoeSizeRepository = shoeSizeRepository;
    }

    @Override
    public void run(String... args) {

        if (this.shoeSizeRepository.count() == 0) {

            Arrays.stream(SizeName.values())
                    .forEach(sizeName -> {
                        ShoeSize shoeSize = new ShoeSize();

                        shoeSize.setSizeName(sizeName);
                        shoeSize.setSize(sizeName.getValue());

                        this.shoeSizeRepository.saveAndFlush(shoeSize);
                    });
        }
    }
}
