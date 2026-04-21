package ru.kuznetsov.shop.generator.scheduler;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.parameter.service.ParameterService;

import java.util.Collections;
import java.util.concurrent.ExecutorService;

@Component
public class SellerScheduler extends AbstractScheduler {

    protected SellerScheduler(ApplicationContext context, ExecutorService executor, ParameterService parameterService) {
        super(context, executor, parameterService);
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void scheduleProductStockUpdateScenario() {
        executor.submit(() -> processScenario("ProductStockUpdateScenario", Collections.emptyMap()));
    }

    @Scheduled(cron = "0 0 * * * *")
    public void scheduleProcessNewOrdersScenario() {
        executor.submit(() -> processScenario("ProcessNewOrdersScenario", Collections.emptyMap()));
    }

    @Scheduled(cron = "0 30 * * * *")
    public void scheduleConfirmOrderPaymentScenario() {
        executor.submit(() -> processScenario("ConfirmOrderPaymentScenario", Collections.emptyMap()));
    }

    @Scheduled(cron = "0 45 * * * *")
    public void scheduleConfirmOrderDeliveryScenario() {
        executor.submit(() -> processScenario("ConfirmOrderDeliveryScenario", Collections.emptyMap()));
    }
}
