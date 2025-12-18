package ru.kuznetsov.shop.generator.service;

import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.generator.connector.WebClientConnector;
import ru.kuznetsov.shop.generator.usecase.UseCase;

import java.util.List;

public abstract class AbstractUseCaseService {

    private final WebClientConnector webClientConnector;

    protected AbstractUseCaseService(WebClient webclient) {
        this.webClientConnector = new WebClientConnector(webclient);
    }

    public <T> List<T> runUseCase(UseCase<T> useCase) {
        return webClientConnector.sendRequest(useCase);
    }
}
