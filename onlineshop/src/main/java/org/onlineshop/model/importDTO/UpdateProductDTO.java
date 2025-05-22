package org.onlineshop.model.importDTO;

import jakarta.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;

public class UpdateProductDTO {

    @NotEmpty(message = "Добавете поне един размер със съответното му количество!")
    private List<QuantitySizeDTO> sizes;

    public UpdateProductDTO() {
        this.sizes = new ArrayList<>();
    }

    public List<QuantitySizeDTO> getSizes() {
        return sizes;
    }

    public void setSizes(List<QuantitySizeDTO> sizes) {
        this.sizes = sizes;
    }
}