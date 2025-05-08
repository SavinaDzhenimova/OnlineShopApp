package org.onlineshop.service.events;

import org.springframework.context.ApplicationEvent;

public class MakeRequestEvent extends ApplicationEvent {

    private String fullName;

    private String email;

    private String phoneNumber;

    private String address;

    private String requestType;

    public MakeRequestEvent(Object source, String fullName, String email, String phoneNumber, String address,
                            String requestType) {
        super(source);
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.requestType = requestType;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
