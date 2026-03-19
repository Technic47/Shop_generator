package ru.kuznetsov.shop.generator.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import ru.kuznetsov.shop.generator.scenario.Scenario;

public abstract class AbstractScheduler {

    protected final ApplicationContext context;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    protected AbstractScheduler(ApplicationContext context) {
        this.context = context;
    }

    protected void processScenario(String scenarioName) {
        try {
            Scenario scenarioBean = (Scenario) context.getBean(scenarioName);
            scenarioBean.run();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
