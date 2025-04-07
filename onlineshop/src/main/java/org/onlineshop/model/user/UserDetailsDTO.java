package org.onlineshop.model.user;

import org.onlineshop.model.entity.DiscountCard;
import org.onlineshop.model.entity.Order;
import org.onlineshop.model.entity.Role;
import org.onlineshop.model.entity.ShoppingCart;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

public class UserDetailsDTO extends User {

    private Long id;

    private String fullName;

    private String address;

    private String phoneNumber;

    private String deliveryAddress;

    private DiscountCard discountCard;

    private BigDecimal totalOutcome;

    private Set<Order> orders;

    private Role role;

    private ShoppingCart shoppingCart;

    public UserDetailsDTO(String email,
                          String password,
                          Collection<? extends GrantedAuthority> authorities,
                          Long id,
                          String fullName,
                          String address,
                          String phoneNumber,
                          String deliveryAddress,
                          DiscountCard discountCard,
                          BigDecimal totalOutcome,
                          Set<Order> orders,
                          Role role,
                          ShoppingCart shoppingCart) {
        super(email, password, authorities);
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.deliveryAddress = deliveryAddress;
        this.discountCard = discountCard;
        this.totalOutcome = totalOutcome;
        this.orders = orders;
        this.role = role;
        this.shoppingCart = shoppingCart;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    public BigDecimal getTotalOutcome() {
        return totalOutcome;
    }

    public void setTotalOutcome(BigDecimal totalOutcome) {
        this.totalOutcome = totalOutcome;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}