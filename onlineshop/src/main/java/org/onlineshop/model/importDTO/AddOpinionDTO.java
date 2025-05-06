package org.onlineshop.model.importDTO;

import jakarta.validation.constraints.*;
import org.onlineshop.model.annotations.ValidEmail;

import java.time.LocalDate;

public class AddOpinionDTO {

    @NotBlank(message = "Моля въведете име и фамилия!")
    @Size(min = 5, max = 50, message = "Името и фамилията трябва да бъдат между 5 и 50 символа!")
    private String author;

    @NotBlank(message = "Моля въведете имейл!")
    @ValidEmail(message = "Имейлът трябва да бъде във формат example@domain.com")
    private String email;

    @NotBlank(message = "Моля въведете мобилен телефон!")
    @Size(min = 7, max = 15, message = "Мобилният телефон трябва да бъде между 7 и 15 символа!")
    private String phoneNumber;

    @NotBlank(message = "Трябва да добавите вашето мнение!")
    @Size(min = 5, max = 500, message = "Мнението трябва да бъде между 5 и 500 символа!")
    private String opinion;

    @NotNull(message = "Трябва да посочите оценка!")
    @Min(value = 1, message = "Оценката трябва да бъде най-малко 1 звезда!")
    @Max(value = 5, message = "Оценката трябва да бъде не повече от 5 звезди!")
    private int rating;

    private LocalDate addedOn;

    public AddOpinionDTO() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }
}