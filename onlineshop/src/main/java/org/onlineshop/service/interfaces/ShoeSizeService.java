package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.ShoeSize;

import java.util.List;
import java.util.Optional;

public interface ShoeSizeService {

    List<ShoeSize> getAllShoeSizes();

    Optional<ShoeSize> getBySize(Integer size);
}
