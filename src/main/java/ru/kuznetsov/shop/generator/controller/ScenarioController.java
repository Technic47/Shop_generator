package ru.kuznetsov.shop.generator.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.shop.generator.scenario.Scenario;

@RestController
@RequestMapping("/run")
@RequiredArgsConstructor
public class ScenarioController {

    private final ApplicationContext context;

    Logger logger = LoggerFactory.getLogger(ScenarioController.class);

    @PostMapping
    public ResponseEntity<Boolean> runScenario(
            @RequestParam String scenario
    ) {
        try {
            String beanName = scenario.substring(0, 1).toLowerCase() + scenario.substring(1);
            Scenario scenarioBean = (Scenario) context.getBean(beanName);
            scenarioBean.run();
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
