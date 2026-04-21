package ru.kuznetsov.shop.generator.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import ru.kuznetsov.shop.generator.scenario.Scenario;
import ru.kuznetsov.shop.parameter.service.ParameterService;

import java.util.Map;
import java.util.concurrent.ExecutorService;

public abstract class AbstractScheduler {

    protected final ApplicationContext context;
    protected final ExecutorService executor;
    protected final ParameterService parameterService;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    protected AbstractScheduler(ApplicationContext context, ExecutorService executor, ParameterService parameterService) {
        this.context = context;
        this.executor = executor;
        this.parameterService = parameterService;
    }

    protected void processScenario(String scenarioName, Map<String, String> parameters) {
        try {
            Scenario scenarioBean = (Scenario) context.getBean(scenarioName.substring(0, 1).toLowerCase() + scenarioName.substring(1));
            scenarioBean.run(parameters);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
