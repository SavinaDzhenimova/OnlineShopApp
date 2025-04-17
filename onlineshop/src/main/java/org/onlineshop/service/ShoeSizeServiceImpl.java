package org.onlineshop.service;

import org.onlineshop.model.entity.ShoeSize;
import org.onlineshop.repository.ShoeSizeRepository;
import org.onlineshop.service.interfaces.ShoeSizeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoeSizeServiceImpl implements ShoeSizeService {

    private final ShoeSizeRepository shoeSizeRepository;

    public ShoeSizeServiceImpl(ShoeSizeRepository shoeSizeRepository) {
        this.shoeSizeRepository = shoeSizeRepository;
    }

    @Override
    public List<ShoeSize> getAllShoeSizes() {
        return this.shoeSizeRepository.findAll();
    }

    @Override
    public Optional<ShoeSize> getBySize(Integer size) {
        return this.shoeSizeRepository.findBySize(size);
    }
}