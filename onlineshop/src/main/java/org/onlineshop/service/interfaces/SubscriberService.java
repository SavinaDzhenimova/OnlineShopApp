package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.Subscriber;

import java.util.Optional;

public interface SubscriberService {

    Result subscribe(String email);

    Optional<Subscriber> getByEmail(String email);

    void saveAndFlush(Subscriber subscriber);
}