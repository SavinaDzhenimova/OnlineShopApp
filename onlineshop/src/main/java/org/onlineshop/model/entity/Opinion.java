package org.onlineshop.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.onlineshop.model.annotations.ValidEmail;

@Entity
@Table(name = "opinions")
public class Opinion extends BaseEntity {

    @Column(nullable = false)
    @Size(min = 5, max = 50)
    private String author;

    @Column(nullable = false)
    @ValidEmail
    private String email;

    @Column(nullable = false, name = "phone_number")
    @Size(min = 7, max = 15)
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Size(min = 5, max = 500)
    private String opinion;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private int rating;

    public Opinion() {
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
}