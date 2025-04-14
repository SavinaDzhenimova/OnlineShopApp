package org.onlineshop.model.user;

import jakarta.validation.constraints.*;
import org.onlineshop.model.annotations.ValidEmail;
import org.onlineshop.model.annotations.ValidPassword;

public class UserRegisterDTO {

    @NotBlank(message = "Моля въведете имейл!")
    @ValidEmail(message = "Имейлът трябва да бъде във формат example@domain.com")
    private String email;

    @ValidPassword(message = "Паролата трябва да бъде между 8 и 20 символа и да съдържа поне една главна буква и поне една цифра!")
    private String password;

    @NotBlank(message = "Потвърждението на паролата е задължително!")
    private String confirmPassword;

    @NotBlank(message = "Моля въведете име и фамилия!")
    @Size(min = 5, max = 50, message = "Името и фамилията трябва да бъдат между 5 и 50 символа!")
    private String fullName;

    @NotBlank(message = "Моля въведете мобилен телефон!")
    @Size(min = 7, max = 15, message = "Мобилният телефон трябва да бъде между 7 и 15 символа!")
    private String phoneNumber;

    @NotBlank(message = "Моля въведете адрес за доставки!")
    @Size(min = 10, max = 70, message = "Адресът трябва да бъде между 10 и 70 символа!")
    private String address;

    private boolean newsletter;

    @AssertTrue(message = "Необходимо е да приемете Политиката за поверителност!")
    private boolean privacyPolicy;

    public UserRegisterDTO() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isNewsletter() {
        return newsletter;
    }

    public void setNewsletter(boolean newsletter) {
        this.newsletter = newsletter;
    }

    public boolean isPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(boolean privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }
}
