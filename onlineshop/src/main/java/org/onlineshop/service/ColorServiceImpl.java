package org.onlineshop.service;

import org.onlineshop.model.entity.Color;
import org.onlineshop.repository.ColorRepository;
import org.onlineshop.service.interfaces.ColorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;

    public ColorServiceImpl(ColorRepository colorRepository) {
        this.colorRepository = colorRepository;
    }

    @Override
    public List<Color> getAllColors() {
        return this.colorRepository.findAll();
    }

    @Override
    public Optional<Color> findColorById(Long id) {
        return this.colorRepository.findById(id);
    }
}