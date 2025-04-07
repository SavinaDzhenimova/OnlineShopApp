package org.onlineshop.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.onlineshop.model.enums.DiscountCardName;

@Entity
@Table(name = "discount_cards")
public class DiscountCard extends BaseEntity {

    @Column(nullable = false, unique = true, name = "discount_card_name")
    private DiscountCardName discountCardName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    public DiscountCard() {
    }

    public DiscountCardName getDiscountCardName() {
        return discountCardName;
    }

    public void setDiscountCardName(DiscountCardName discountCardName) {
        this.discountCardName = discountCardName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
