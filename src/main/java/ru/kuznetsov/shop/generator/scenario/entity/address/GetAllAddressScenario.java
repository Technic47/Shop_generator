package ru.kuznetsov.shop.generator.scenario.entity.address;

import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.scenario.AbstractScenario;
import ru.kuznetsov.shop.generator.service.UseCaseService;
import ru.kuznetsov.shop.generator.usecase.auth.GetTokenUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.address.GetAllAddressUseCase;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;

import static ru.kuznetsov.shop.generator.common.ConstValues.ADMIN_LOGIN;
import static ru.kuznetsov.shop.generator.common.ConstValues.ADMIN_PASSWORD;

@Component
public class GetAllAddressScenario extends AbstractScenario {

    protected GetAllAddressScenario(UseCaseService useCaseService) {
        super(useCaseService);
    }

    @Override
    public void run() {
        TokenDto token = runUseCaseWithReturn(new GetTokenUseCase(ADMIN_LOGIN, ADMIN_PASSWORD)).get(0);
        runUseCase(new GetAllAddressUseCase(token.getToken()));
    }
}
