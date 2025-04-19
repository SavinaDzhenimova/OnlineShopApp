package org.onlineshop.model.exportDTO;

import java.util.List;

public class PromoCodesListDTO {

    private List<PromoCodeDTO> promoCodes;

    public PromoCodesListDTO(List<PromoCodeDTO> promoCodesList) {
        this.promoCodes = promoCodesList;
    }

    public List<PromoCodeDTO> getPromoCodes() {
        return promoCodes;
    }

    public void setPromoCodes(List<PromoCodeDTO> promoCodes) {
        this.promoCodes = promoCodes;
    }
}
