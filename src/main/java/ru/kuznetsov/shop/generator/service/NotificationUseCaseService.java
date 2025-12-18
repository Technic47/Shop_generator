package ru.kuznetsov.shop.generator.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NotificationUseCaseService extends AbstractUseCaseService {

    protected NotificationUseCaseService(@Qualifier("notification") WebClient webclient) {
        super(webclient);
    }
}
