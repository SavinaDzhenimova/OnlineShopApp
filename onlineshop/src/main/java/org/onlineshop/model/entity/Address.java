package org.onlineshop.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.onlineshop.model.enums.AddressType;
import org.onlineshop.model.enums.Region;

import java.util.Objects;

@Entity
@Table(name = "addresses")
public class Address extends BaseEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    @Column(nullable = false)
    @Size(min = 4, max = 15)
    private String town;

    @Column(nullable = false)
    @Pattern(regexp = "\\d{4}")
    private String postalCode;

    @Column(nullable = false)
    @Size(min = 5, max = 30)
    private String street;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Address() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return region == address.region && Objects.equals(town, address.town) && Objects.equals(postalCode, address.postalCode) && Objects.equals(street, address.street) && Objects.equals(user, address.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(region, town, postalCode, street, user);
    }
}
