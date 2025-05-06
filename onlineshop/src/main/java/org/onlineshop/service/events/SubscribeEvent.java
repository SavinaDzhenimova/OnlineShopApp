package org.onlineshop.service.events;

import org.springframework.context.ApplicationEvent;

public class SubscribeEvent extends ApplicationEvent {

    private String email;

    public SubscribeEvent(Object source, String email) {
        super(source);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}