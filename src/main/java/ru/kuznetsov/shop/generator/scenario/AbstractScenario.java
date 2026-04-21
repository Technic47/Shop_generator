package ru.kuznetsov.shop.generator.scenario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kuznetsov.shop.generator.service.GateUseCaseService;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.generator.usecase.auth.GetTokenUseCase;
import ru.kuznetsov.shop.generator.usecase.auth.GetUserInfoUseCase;
import ru.kuznetsov.shop.parameter.service.ParameterService;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;
import ru.kuznetsov.shop.represent.dto.auth.UserDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.kuznetsov.shop.generator.common.ConstValues.PARAMETER_LOGIN;
import static ru.kuznetsov.shop.generator.common.ConstValues.PARAMETER_PASSWORD;

public abstract class AbstractScenario implements Scenario {

    protected final GateUseCaseService gateUseCaseService;
    protected final ParameterService parameterService;

    Logger logger = LoggerFactory.getLogger(AbstractScenario.class);

    protected AbstractScenario(GateUseCaseService gateUseCaseService, ParameterService parameterService) {
        this.gateUseCaseService = gateUseCaseService;
        this.parameterService = parameterService;
    }

    protected <T> List<T> runUseCaseWithReturn(UseCase<T> useCase) {
        return gateUseCaseService.runUseCase(useCase);
    }

    protected void runUseCase(UseCase<?> useCase) {
        gateUseCaseService.runUseCase(useCase);
    }

    protected UserDto getUserInfo(String tokenString){
        logger.info("Getting user");
        UserDto userDto = runUseCaseWithReturn(new GetUserInfoUseCase(tokenString)).get(0);
        logger.info("UserInfo: {}, {}", userDto.getUsername(), userDto.getEmail());

        return userDto;
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
