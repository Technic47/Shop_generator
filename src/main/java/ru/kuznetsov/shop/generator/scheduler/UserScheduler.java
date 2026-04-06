package ru.kuznetsov.shop.generator.scheduler;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutorService;

import static ru.kuznetsov.shop.generator.common.ConstValues.*;

@Component
public class UserScheduler extends AbstractScheduler {


    protected UserScheduler(ApplicationContext context, ExecutorService executor) {
        super(context, executor);
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void scheduleCreateOrderScenario() {
        var parameters = Map.of(
                PARAMETER_LOGIN, USER_LOGIN,
                PARAMETER_PASSWORD, USER_PASSWORD
        );
        var parameters2 = Map.of(
                PARAMETER_LOGIN, USER2_LOGIN,
                PARAMETER_PASSWORD, USER2_PASSWORD
        );
        var parameters3 = Map.of(
                PARAMETER_LOGIN, USER3_LOGIN,
                PARAMETER_PASSWORD, USER3_PASSWORD
        );
        var parameters4 = Map.of(
                PARAMETER_LOGIN, USER4_LOGIN,
                PARAMETER_PASSWORD, USER4_PASSWORD
        );
        executor.submit(() -> processScenario("CreateOrderScenario", parameters));
        executor.submit(() -> processScenario("CreateOrderScenario", parameters2));
        executor.submit(() -> processScenario("CreateOrderScenario", parameters3));
        executor.submit(() -> processScenario("CreateOrderScenario", parameters4));
    }
}
