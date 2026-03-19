package ru.kuznetsov.shop.generator.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ShipmentUseCaseService extends AbstractUseCaseService {
    protected ShipmentUseCaseService(@Qualifier("shipment") WebClient webclient) {
        super(webclient);
    }
}
