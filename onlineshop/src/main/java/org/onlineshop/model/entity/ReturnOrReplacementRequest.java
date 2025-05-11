package org.onlineshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.onlineshop.model.annotations.ValidEmail;
import org.onlineshop.model.enums.AddressType;
import org.onlineshop.model.enums.Region;
import org.onlineshop.model.enums.RequestType;

import java.time.LocalDateTime;

@Entity
@Table(name = "return_or_replacement_requests")
public class ReturnOrReplacementRequest extends BaseEntity {

    @Column(nullable = false, name = "full_name")
    @Size(min = 5, max = 50)
    private String fullName;

    @Column(nullable = false, unique = true)
    @ValidEmail
    private String email;

    @Column(nullable = false, unique = true, name = "phone_number")
    @Size(min = 7, max = 15)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(nullable = false)
    @Size(min = 4, max = 15)
    private String town;

    @Column(nullable = false, name = "postal_code")
    @Pattern(regexp = "\\d{4}")
    private String postalCode;

    @Column(nullable = false)
    @Size(min = 5, max = 30)
    private String street;

    @Column(nullable = false, name = "address_type")
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @Column(nullable = false, name = "request_type")
    @Enumerated(EnumType.STRING)
    private RequestType requestType;

    @Column(nullable = false, name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "completed_on")
    private LocalDateTime completedOn;

    @Column(nullable = false)
    private boolean completed;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public ReturnOrReplacementRequest() {
        this.completed = false;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
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

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getCompletedOn() {
        return completedOn;
    }

    public void setCompletedOn(LocalDateTime completedOn) {
        this.completedOn = completedOn;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}