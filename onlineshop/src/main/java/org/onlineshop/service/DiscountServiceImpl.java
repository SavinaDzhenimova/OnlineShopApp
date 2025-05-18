package org.onlineshop.service;

import org.onlineshop.model.entity.Discount;
import org.onlineshop.model.entity.Product;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.importDTO.AddDiscountDTO;
import org.onlineshop.repository.DiscountRepository;
import org.onlineshop.repository.ProductRepository;
import org.onlineshop.service.interfaces.DiscountService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository, ProductRepository productRepository) {
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
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

        Optional<Product> optionalProduct = this.productRepository.findById(id);

        if (optionalProduct.isEmpty()) {
            throw new NoSuchElementException("Този продукт не съществува!");
        }

        Product product = optionalProduct.get();

        product.setOnSale(true);
        product.setOldPrice(product.getPrice());

        BigDecimal newPrice = product.getPrice().subtract(product.getPrice()
                .multiply(discountPercent.divide(BigDecimal.valueOf(100))));
        product.setPrice(newPrice);

        this.productRepository.saveAndFlush(product);
    }
}