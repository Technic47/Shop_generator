package ru.kuznetsov.shop.generator.scheduler;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SellerScheduler extends AbstractScheduler {

    protected SellerScheduler(ApplicationContext context) {
        super(context);
    }

    @Scheduled(cron = "0 0 12 * * *")
    public void scheduleProductStockUpdateScenario() {
        processScenario("ProductStockUpdateScenario");
    }

    @Scheduled(cron = "0 30 12 * * *")
    public void scheduleProcessNewOrdersScenario() {
        processScenario("ProcessNewOrdersScenario");
    }

    @Scheduled(cron = "0 0 13 * * *")
    public void scheduleConfirmOrderPaymentScenario() {
        processScenario("ConfirmOrderPaymentScenario");
    }

    @Scheduled(cron = "0 10 13 * * *")
    public void scheduleConfirmOrderDeliveryScenario() {
        processScenario("ConfirmOrderDeliveryScenario");
    }
}
