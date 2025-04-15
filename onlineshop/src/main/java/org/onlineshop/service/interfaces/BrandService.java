package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> getAllBrands();

    Optional<Brand> getBrandById(Long id);
}
