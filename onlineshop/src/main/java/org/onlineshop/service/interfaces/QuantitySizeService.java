package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.QuantitySize;

public interface QuantitySizeService {

    void saveAndFlush(QuantitySize quantitySize);
}
