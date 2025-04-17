package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Color;

import java.util.List;
import java.util.Optional;

public interface ColorService {

    List<Color> getAllColors();

    Optional<Color> findColorById(Long id);
}
