package ru.kuznetsov.shop.generator.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.kuznetsov.shop.generator.connector.WebClientConnector;
import ru.kuznetsov.shop.generator.usecase.UseCase;

import java.util.List;

@Service
public class UseCaseService {

    private final WebClientConnector webClientConnector;

    public UseCaseService(WebClient webclient) {
        this.webClientConnector = new WebClientConnector(webclient);
    }

    public <T> List<T> runUseCase(UseCase<T> useCase) {
        return webClientConnector.sendRequest(useCase);
    }
}
