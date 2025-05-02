package org.onlineshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.onlineshop.model.annotations.ValidEmail;
import org.onlineshop.model.enums.AddressType;
import org.onlineshop.model.enums.OrderStatus;
import org.onlineshop.model.enums.Region;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Column(nullable = false, name = "full_name")
    @Size(min = 5, max = 50)
    private String fullName;

    @Column(nullable = false)
    @ValidEmail
    private String email;

    @Column(nullable = false, name = "phone_number")
    @Size(min = 7, max = 15)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(nullable = false, name = "address_type")
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @Column(nullable = false)
    @Size(min = 4, max = 15)
    private String town;

    @Column(nullable = false, name = "postal_code")
    @Pattern(regexp = "\\d{4}")
    private String postalCode;

    @Column(nullable = false)
    @Size(min = 5, max = 30)
    private String street;

    @Column(nullable = false, name = "ordered_on")
    private LocalDateTime orderedOn;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "delivered_on")
    private LocalDateTime deliveredOn;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, name = "total_price")
    @Positive
    private BigDecimal totalPrice;

    @Column
    @PositiveOrZero
    private BigDecimal discount;

    @Column(name = "vip_status_discount")
    @PositiveOrZero
    private BigDecimal vipStatusDiscount;

    @Column(name = "final_price")
    @PositiveOrZero
    private BigDecimal finalPrice;

    @ManyToOne
    @JoinColumn(name = "promo_code_id", referencedColumnName = "id")
    private PromoCode promoCode;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> orderItems;

    public Order() {
        this.orderItems = new ArrayList<>();
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

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public LocalDateTime getOrderedOn() {
        return orderedOn;
    }

    public void setOrderedOn(LocalDateTime orderedOn) {
        this.orderedOn = orderedOn;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getDeliveredOn() {
        return deliveredOn;
    }

    public void setDeliveredOn(LocalDateTime deliveredOn) {
        this.deliveredOn = deliveredOn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getVipStatusDiscount() {
        return vipStatusDiscount;
    }

    public void setVipStatusDiscount(BigDecimal vipStatusDiscount) {
        this.vipStatusDiscount = vipStatusDiscount;
    }

    public BigDecimal getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(BigDecimal finalPrice) {
        this.finalPrice = finalPrice;
    }

    public PromoCode getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
