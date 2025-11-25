package ru.kuznetsov.shop.generator.scenario.entity.address;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.scenario.Scenario;
import ru.kuznetsov.shop.generator.service.UseCaseService;
import ru.kuznetsov.shop.generator.usecase.auth.GetTokenUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.address.GetAllAddressUseCase;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;

import static ru.kuznetsov.shop.generator.common.ConstValues.ADMIN_LOGIN;
import static ru.kuznetsov.shop.generator.common.ConstValues.ADMIN_PASSWORD;

@Component
@RequiredArgsConstructor
public class GetAllAddressScenario implements Scenario {

    private final UseCaseService useCaseService;

    @Override
    public void run() {
        TokenDto token = useCaseService.runUseCase(new GetTokenUseCase(ADMIN_LOGIN, ADMIN_PASSWORD)).getFirst();
        useCaseService.runUseCase(new GetAllAddressUseCase(token.getToken()));
    }
}
