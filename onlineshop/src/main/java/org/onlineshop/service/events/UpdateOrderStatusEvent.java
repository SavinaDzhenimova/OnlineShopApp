package org.onlineshop.service.events;

import org.springframework.context.ApplicationEvent;

public class UpdateOrderStatusEvent extends ApplicationEvent {

    private Long id;

    private String fullName;

    private String email;

    private String previousStatus;

    private String currentStatus;

    public UpdateOrderStatusEvent(Object source, Long id, String fullName, String email, String previousStatus,
                                  String currentStatus) {
        super(source);
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.previousStatus = previousStatus;
        this.currentStatus = currentStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }
}
