package org.onlineshop.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.onlineshop.model.annotations.ValidEmail;

@Entity
@Table(name = "subscribers")
public class Subscriber extends BaseEntity {

    @Column(nullable = false, unique = true)
    @ValidEmail
    private String email;

    public Subscriber() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}