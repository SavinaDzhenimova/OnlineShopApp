package org.onlineshop.service.events;

import org.springframework.context.ApplicationEvent;

public class ForgotPasswordEvent extends ApplicationEvent {

    private String fullName;

    private String email;

    private String token;

    public ForgotPasswordEvent(Object source, String fullName, String email, String token) {
        super(source);
        this.fullName = fullName;
        this.email = email;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}