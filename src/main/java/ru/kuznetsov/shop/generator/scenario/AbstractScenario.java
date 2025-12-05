package ru.kuznetsov.shop.generator.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuznetsov.shop.generator.service.UseCaseService;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.generator.usecase.auth.GetTokenUseCase;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;

import java.util.List;

public abstract class AbstractScenario implements Scenario {

    protected final UseCaseService useCaseService;

    Logger logger = LoggerFactory.getLogger(AbstractScenario.class);

    protected AbstractScenario(UseCaseService useCaseService) {
        this.useCaseService = useCaseService;
    }

    protected <T> List<T> runUseCaseWithReturn(UseCase<T> useCase) {
        return useCaseService.runUseCase(useCase);
    }

    protected void runUseCase(UseCase<?> useCase) {
        useCaseService.runUseCase(useCase);
    }

    protected TokenDto getToken(String login, String password) {
        logger.info("Getting token");
        TokenDto token = runUseCaseWithReturn(new GetTokenUseCase(login, password)).get(0);
        logger.info("Token String: {}", token.getToken());
        return token;
    }
}
