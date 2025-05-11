package org.onlineshop.service.events;

import org.springframework.context.ApplicationEvent;

public class SendInquiryEvent extends ApplicationEvent {

    private String fullName;

    private String email;

    private String phoneNumber;

    private String message;

    public SendInquiryEvent(Object source, String fullName, String email, String phoneNumber, String message) {
        super(source);
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.message = message;
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