package org.onlineshop.service;

import org.onlineshop.model.entity.Result;
import org.onlineshop.model.entity.Subscriber;
import org.onlineshop.repository.SubscriberRepository;
import org.onlineshop.service.events.SubscribeEvent;
import org.onlineshop.service.interfaces.SubscriberService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public SubscriberServiceImpl(SubscriberRepository subscriberRepository,
                                 ApplicationEventPublisher applicationEventPublisher) {
        this.subscriberRepository = subscriberRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Result subscribe(String email) {

        Optional<Subscriber> optionalSubscriber = this.subscriberRepository.findByEmail(email);

        if (optionalSubscriber.isPresent()) {
            return new Result(false, "Въведеният имейл адрес вече е регистриран!");
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(email);

        this.subscriberRepository.saveAndFlush(subscriber);

        this.applicationEventPublisher.publishEvent(new SubscribeEvent(this, email));

        return new Result(true, "На твоя имейл успешно е изпратен промо код!");
    }

    @Override
    public Optional<Subscriber> getByEmail(String email) {
        return this.subscriberRepository.findByEmail(email);
    }

    @Override
    public void saveAndFlush(Subscriber subscriber) {
        this.subscriberRepository.saveAndFlush(subscriber);
    }
}