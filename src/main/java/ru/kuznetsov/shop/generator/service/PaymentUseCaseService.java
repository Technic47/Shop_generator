package ru.kuznetsov.shop.generator.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class PaymentUseCaseService extends AbstractUseCaseService {
    protected PaymentUseCaseService(@Qualifier("payment") WebClient webclient) {
        super(webclient);
    }
}
