package org.onlineshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.onlineshop.model.annotations.ValidEmail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, name = "full_name")
    @Size(min = 5, max = 50)
    private String fullName;

    @Column(nullable = false, unique = true)
    @ValidEmail
    private String email;

    @Column(nullable = false, unique = true)
    @Size(min = 7, max = 15)
    private String phoneNumber;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Address> addresses;

    @Column(nullable = false)
    @Size(min = 8)
    private String password;

    @ManyToOne
    @JoinColumn(name = "discount_card_id", referencedColumnName = "id")
    private DiscountCard discountCard;

    @Column(nullable = false, name = "total_outcome")
    @PositiveOrZero
    private BigDecimal totalOutcome;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Order> orders;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private ShoppingCart shoppingCart;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_favourites",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private Set<Product> favourites;

    public User() {
        this.addresses = new ArrayList<>();
        this.orders = new HashSet<>();
        this.favourites = new HashSet<>();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Set<Product> getFavourites() {
        return favourites;
    }

    public void setFavourites(Set<Product> favourites) {
        this.favourites = favourites;
    }
}
