package ru.kuznetsov.shop.generator.scheduler;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserScheduler extends AbstractScheduler {

    protected UserScheduler(ApplicationContext context) {
        super(context);
    }

    @Scheduled(cron = "0 /30 * * * *")
    public void scheduleCreateOrderScenario() {
        processScenario("CreateOrderScenario");
    }
}
