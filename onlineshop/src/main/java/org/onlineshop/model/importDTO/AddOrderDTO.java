package org.onlineshop.model.importDTO;

import jakarta.validation.constraints.*;
import org.onlineshop.model.annotations.ValidEmail;
import org.onlineshop.model.enums.AddressType;
import org.onlineshop.model.enums.Region;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddOrderDTO {

    @NotNull
    @Size(min = 5, max = 50)
    private String fullName;

    @NotNull
    @ValidEmail
    private String email;

    @NotNull
    @Size(min = 7, max = 15)
    private String phoneNumber;

    @NotNull(message = "Моля изберете област!")
    private Region region;

    @NotNull(message = "Моля изберете вид на адреса!")
    private AddressType addressType;

    @NotNull(message = "Моля въведете име на град!")
    @Size(min = 4, max = 15, message = "Името на градът трябва да бъде между 4 и 15 символа!")
    private String town;

    @NotNull(message = "Моля въведете пощенски код!")
    @Pattern(regexp = "\\d{4}", message = "Пощенският код трябва да съдържа точно 4 цифри!")
    private String postalCode;

    @NotNull(message = "Моля въведете име на улица!")
    @Size(min = 5, max = 30, message = "Името на улицата трябва да бъде между 5 и 30 символа!")
    private String street;

    @NotNull
    @Positive
    private BigDecimal totalPrice;

    private String promoCode;

    private BigDecimal discountPercent;

    private BigDecimal discount;

    private BigDecimal vipStatusDiscount;

    private BigDecimal finalPrice;

    @NotEmpty
    private List<AddOrderItemDTO> orderItems;

    public AddOrderDTO() {
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

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
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

    public List<AddOrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<AddOrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
