package org.onlineshop.init;

import org.onlineshop.model.entity.Brand;
import org.onlineshop.model.enums.BrandName;
import org.onlineshop.repository.BrandRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BrandsInit implements CommandLineRunner {

    private final BrandRepository brandRepository;

    public BrandsInit(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public void run(String... args) {

        if (this.brandRepository.count() == 0) {

            Arrays.stream(BrandName.values())
                    .forEach(brandName -> {
                        Brand brand = new Brand();
                        brand.setBrandName(brandName);

                        this.brandRepository.saveAndFlush(brand);
                    });
        }
    }
}
