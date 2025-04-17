package org.onlineshop.model.exportDTO;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartDTO {

    private List<CartItemDTO> cartItems;

    private BigDecimal totalPrice;

    public ShoppingCartDTO(List<CartItemDTO> cartItems, BigDecimal totalPrice) {
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
    }

    public List<CartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
