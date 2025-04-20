package org.onlineshop.model.user;

import jakarta.validation.constraints.Size;
import org.onlineshop.model.annotations.ValidEmail;

import java.math.BigDecimal;

public class UserDTO {

    @Size(min = 5, max = 50, message = "Името трябва да бъде между 5 и 50 символа!")
    private String fullName;

    @ValidEmail(message = "Имейлът трябва да бъде във формат example@domain.com")
    private String email;

    @Size(min = 7, max = 15, message = "Мобилният телефон трябва да бъде между 7 и 15 символа!")
    private String phoneNumber;

    public UserDTO() {
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
}
