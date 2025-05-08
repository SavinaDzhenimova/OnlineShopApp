package org.onlineshop.model.importDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.onlineshop.model.annotations.ValidEmail;
import org.onlineshop.model.enums.AddressType;
import org.onlineshop.model.enums.Region;
import org.onlineshop.model.enums.RequestType;

public class AddReturnOrReplacementRequestDTO {

    @NotBlank(message = "Моля въведете име и фамилия!")
    @Size(min = 5, max = 50, message = "Името и фамилията трябва да бъдат между 5 и 50 символа!")
    private String fullName;

    @NotBlank(message = "Моля въведете имейл!")
    @ValidEmail(message = "Имейлът трябва да бъде във формат example@domain.com")
    private String email;

    @NotBlank(message = "Моля въведете мобилен телефон!")
    @Size(min = 7, max = 15, message = "Мобилният телефон трябва да бъде между 7 и 15 символа!")
    private String phoneNumber;

    @NotNull(message = "Моля изберете област!")
    private Region region;

    @NotNull(message = "Моля въведете име на град!")
    @Size(min = 4, max = 15, message = "Името на градът трябва да бъде между 4 и 15 символа!")
    private String town;

    @NotNull(message = "Моля въведете пощенски код!")
    @Pattern(regexp = "\\d{4}", message = "Пощенският код трябва да съдържа точно 4 цифри!")
    private String postalCode;

    @NotNull(message = "Моля въведете име на улица!")
    @Size(min = 5, max = 30, message = "Името на улицата трябва да бъде между 5 и 30 символа!")
    private String street;

    @NotNull(message = "Моля изберете вид на адреса!")
    private AddressType addressType;

    @NotNull(message = "Моля изберете замяна или връщане искате да извършите!")
    private RequestType requestType;

    public AddReturnOrReplacementRequestDTO() {
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
}
