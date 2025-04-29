package org.onlineshop.model.importDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.onlineshop.model.enums.AddressType;
import org.onlineshop.model.enums.Region;

public class AddAddressDTO {

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

    public AddAddressDTO() {
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
}
