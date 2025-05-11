package org.onlineshop.model.importDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.onlineshop.model.annotations.ValidEmail;

public class AddInquiryDTO {

    @NotBlank(message = "Моля въведете име и фамилия!")
    @Size(min = 5, max = 50, message = "Името и фамилията трябва да бъдат между 5 и 50 символа!")
    private String fullName;

    @NotBlank(message = "Моля въведете имейл!")
    @ValidEmail(message = "Имейлът трябва да бъде във формат example@domain.com")
    private String email;

    @NotBlank(message = "Моля въведете мобилен телефон!")
    @Size(min = 7, max = 15, message = "Мобилният телефон трябва да бъде между 7 и 15 символа!")
    private String phoneNumber;

    @NotBlank(message = "Трябва да добавите съобщение!")
    @Size(min = 5, max = 5000, message = "Съобщението трябва да бъде между 5 и 5000 символа!")
    private String message;

    public AddInquiryDTO() {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
