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

                        String brandUrl = "";

                        switch (brandName) {
                            case ADIDAS -> brandUrl = "/images/brands/brand-adidas-dark.svg";
                            case ASICS -> brandUrl = "/images/brands/brand-asics-dark.svg";
                            case CALVIN_KLEIN -> brandUrl = "/images/brands/brand-calvin-klein-dark.svg";
                            case CATERPILLAR -> brandUrl = "/images/brands/brand-caterpillar-dark.svg";
                            case CHAMPION -> brandUrl = "/images/brands/brand-champion-dark.svg";
                            case COLUMBIA -> brandUrl = "/images/brands/brand-columbia-dark.svg";
                            case CONVERSE -> brandUrl = "/images/brands/brand-converse-dark.svg";
                            case CROCKS -> brandUrl = "/images/brands/brand-crocks-dark.svg";
                            case DIADORA -> brandUrl = "/images/brands/brand-diadora-dark.svg";
                            case FILA -> brandUrl = "/images/brands/brand-fila-dark.svg";
                            case GUESS -> brandUrl = "/images/brands/brand-guess-dark.svg";
                            case KAPPA -> brandUrl = "/images/brands/brand-kappa-dark.svg";
                            case LACOSTE -> brandUrl = "/images/brands/brand-lacoste-dark.svg";
                            case LOTTO -> brandUrl = "/images/brands/brand-lotto-dark.svg";
                            case NAPAPIJRI -> brandUrl = "/images/brands/brand-napapijri-dark.svg";
                            case NEW_BALANCE -> brandUrl = "/images/brands/brand-new-balance-dark.svg";
                            case NIKE -> brandUrl = "/images/brands/brand-nike-dark.svg";
                            case PALLADIUM -> brandUrl = "/images/brands/brand-palladium-dark.svg";
                            case PUMA -> brandUrl = "/images/brands/brand-puma-dark.svg";
                            case REEBOK -> brandUrl = "/images/brands/brand-reebok-dark.svg";
                            case REEF -> brandUrl = "/images/brands/brand-reef-dark.svg";
                            case RIP_CURL -> brandUrl = "/images/brands/brand-rip-curl-dark.svg";
                            case SOLOMON -> brandUrl = "/images/brands/brand-solomon-dark.svg";
                            case SKECKERS -> brandUrl = "/images/brands/brand-skechers-dark.svg";
                            case THE_NORTH_FACE -> brandUrl = "/images/brands/brand-the-north-face-dark.svg";
                            case TIMBERLAND -> brandUrl = "/images/brands/brand-timberland-dark.svg";
                            case TOMMY_HILFIGER -> brandUrl = "/images/brands/brand-tommy-hilfiger-dark.svg";
                            case UNDER_ARMOR -> brandUrl = "/images/brands/brand-under-armor-dark.svg";
                            case US_POLO_ASSN -> brandUrl = "/images/brands/brand-us-polo-assn-dark.svg";
                        }

                        brand.setBrandUrl(brandUrl);

                        this.brandRepository.saveAndFlush(brand);
                    });
        }
    }
}
