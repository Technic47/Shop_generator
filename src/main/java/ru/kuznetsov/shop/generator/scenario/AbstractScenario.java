package ru.kuznetsov.shop.generator.scenario;

import ru.kuznetsov.shop.generator.service.UseCaseService;
import ru.kuznetsov.shop.generator.usecase.UseCase;

import java.util.List;

public abstract class AbstractScenario implements Scenario {

    protected final UseCaseService useCaseService;

    protected AbstractScenario(UseCaseService useCaseService) {
        this.useCaseService = useCaseService;
    }

    protected <T> List<T> runUseCaseWithReturn(UseCase<T> useCase) {
        return useCaseService.runUseCase(useCase);
    }

    protected void runUseCase(UseCase<?> useCase) {
        useCaseService.runUseCase(useCase);
    }
}
