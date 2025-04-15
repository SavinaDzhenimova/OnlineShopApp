package org.onlineshop.service;

import org.onlineshop.model.entity.QuantitySize;
import org.onlineshop.repository.QuantitySizeRepository;
import org.onlineshop.service.interfaces.QuantitySizeService;
import org.springframework.stereotype.Service;

@Service
public class QuantitySizeServiceImpl implements QuantitySizeService {

    private final QuantitySizeRepository quantitySizeRepository;

    public QuantitySizeServiceImpl(QuantitySizeRepository quantitySizeRepository) {
        this.quantitySizeRepository = quantitySizeRepository;
    }

    @Override
    public void saveAndFlush(QuantitySize quantitySize) {
        this.quantitySizeRepository.saveAndFlush(quantitySize);
    }
}