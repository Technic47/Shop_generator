package ru.kuznetsov.shop.generator.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuznetsov.shop.generator.service.GateUseCaseService;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.generator.usecase.auth.GetTokenUseCase;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.kuznetsov.shop.generator.common.ConstValues.PARAMETER_LOGIN;
import static ru.kuznetsov.shop.generator.common.ConstValues.PARAMETER_PASSWORD;

public abstract class AbstractScenario implements Scenario {

    protected final GateUseCaseService gateUseCaseService;

    Logger logger = LoggerFactory.getLogger(AbstractScenario.class);

    protected AbstractScenario(GateUseCaseService gateUseCaseService) {
        this.gateUseCaseService = gateUseCaseService;
    }

    protected <T> List<T> runUseCaseWithReturn(UseCase<T> useCase) {
        return gateUseCaseService.runUseCase(useCase);
    }

    protected void runUseCase(UseCase<?> useCase) {
        gateUseCaseService.runUseCase(useCase);
    }


    protected TokenDto getToken(Map<String, String> parameters, String defaultLogin, String defaultPassword) {
        logger.info("Getting token");
        String login = getParameter(parameters, PARAMETER_LOGIN).orElse(defaultLogin);
        String password = getParameter(parameters, PARAMETER_PASSWORD).orElse(defaultPassword);
        TokenDto token = runUseCaseWithReturn(new GetTokenUseCase(login, password)).get(0);
        logger.info("Token String: {}", token.getToken());
        return token;
    }

    protected static Optional<String> getParameter(Map<String, String> parameters, String paramName) {
        return Optional.ofNullable(parameters.get(paramName));
    }
}
