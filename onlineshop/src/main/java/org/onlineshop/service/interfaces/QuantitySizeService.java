package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.QuantitySize;
import org.onlineshop.model.entity.ShoeSize;

import java.util.Optional;

public interface QuantitySizeService {

    void saveAndFlush(QuantitySize quantitySize);
}
