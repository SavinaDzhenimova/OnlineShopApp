package org.onlineshop.service;

import org.onlineshop.model.entity.Brand;
import org.onlineshop.repository.BrandRepository;
import org.onlineshop.service.interfaces.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> getAllBrands() {
        return this.brandRepository.findAll();
    }

    @Override
    public Optional<Brand> getBrandById(Long id) {
        return this.brandRepository.findById(id);
    }
}
