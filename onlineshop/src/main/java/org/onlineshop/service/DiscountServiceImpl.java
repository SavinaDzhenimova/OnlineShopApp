package org.onlineshop.service;

import org.onlineshop.model.entity.Discount;
import org.onlineshop.model.entity.Product;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.importDTO.AddDiscountDTO;
import org.onlineshop.repository.DiscountRepository;
import org.onlineshop.service.interfaces.DiscountService;
import org.onlineshop.service.interfaces.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductService productService;

    public DiscountServiceImpl(DiscountRepository discountRepository, ProductService productService) {
        this.discountRepository = discountRepository;
        this.productService = productService;
    }

    @Override
    public Result addDiscount(AddDiscountDTO addDiscountDTO) {

        Discount discount = new Discount();

        discount.setDiscountPercent(addDiscountDTO.getDiscountPercent());
        discount.setValidFrom(addDiscountDTO.getValidFrom());
        discount.setValidTo(addDiscountDTO.getValidTo());
        discount.setMinPrice(addDiscountDTO.getMinPrice());
        discount.setMaxPrice(addDiscountDTO.getMaxPrice());

        this.discountRepository.saveAndFlush(discount);
        return new Result(true, "Успешно добавихте отстъпка!");
    }

    public void makeOnSale(Long id, BigDecimal discountPercent) {

        Optional<Product> optionalProduct = this.productService.getById(id);

        if (optionalProduct.isEmpty()) {
            throw new NoSuchElementException("Този продукт не съществува!");
        }

        Product product = optionalProduct.get();

        product.setOnSale(true);
        product.setOldPrice(product.getPrice());

        BigDecimal newPrice = product.getPrice().subtract(product.getPrice()
                .multiply(discountPercent.divide(BigDecimal.valueOf(100))));
        product.setPrice(newPrice);

        this.productService.saveAndFlush(product);
    }

    @Override
    public void applyDiscounts() {
        LocalDate today = LocalDate.now();

        List<Discount> activeDiscounts = this.discountRepository
                .findAllByValidFromLessThanEqualAndValidToGreaterThanEqual(today, today);
        List<Product> products = this.productService.getAll();

        for (Product product : products) {

            if (product.isOnSale()) {
                product.setPrice(product.getOldPrice());
                product.setOldPrice(null);
                product.setOnSale(false);
                product.setSalePercent(null);

                this.productService.saveAndFlush(product);
            }

            for (Discount discount : activeDiscounts) {
                if (product.getPrice().compareTo(discount.getMinPrice()) >= 0 &&
                        product.getPrice().compareTo(discount.getMaxPrice()) <= 0) {

                    if (!product.isOnSale()) {
                        product.setOldPrice(product.getPrice());

                        BigDecimal newPrice = product.getPrice().multiply(
                                BigDecimal.ONE.subtract(discount.getDiscountPercent().divide(BigDecimal.valueOf(100))));

                        product.setPrice(newPrice);
                        product.setOnSale(true);
                        product.setSalePercent(discount.getDiscountPercent().intValue());

                        this.productService.saveAndFlush(product);
                    }

                    break;
                }
            }
        }
    }
}