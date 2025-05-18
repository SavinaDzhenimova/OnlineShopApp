package org.onlineshop.service.scheduler;

import org.onlineshop.model.entity.Product;
import org.onlineshop.repository.ProductRepository;
import org.onlineshop.service.interfaces.DiscountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CronScheduler {

    private final Logger LOGGER = LoggerFactory.getLogger(CronScheduler.class);

    private final ProductRepository productRepository;
    private final DiscountService discountService;

    public CronScheduler(ProductRepository productRepository, DiscountService discountService) {
        this.productRepository = productRepository;
        this.discountService = discountService;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateIsNewFlag() {
        LocalDate twoWeeksAgo = LocalDate.now().minusWeeks(2);

        List<Product> products = this.productRepository.findAllByIsNewTrueAndAddedOnBefore(twoWeeksAgo);

        for (Product product : products) {
            product.setNew(false);
        }

        this.productRepository.saveAllAndFlush(products);
    }

    @Scheduled(cron = "0 0 13 * * *")
    public void scheduledDiscountCheck() {
        this.discountService.applyDiscounts();

        this.LOGGER.info("Отстъпките са приложени успешно!");
    }
}