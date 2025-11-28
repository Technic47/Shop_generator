package ru.kuznetsov.shop.generator.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.shop.generator.scenario.Scenario;

@RestController
@RequestMapping("/run")
@RequiredArgsConstructor
public class ScenarioController {

    private final ApplicationContext context;

    @PostMapping
    public ResponseEntity<Boolean> runGetAllAddress(
            @RequestParam String scenario
    ) {
        try {
            String beanName = scenario.substring(0, 1).toLowerCase() + scenario.substring(1);
            Scenario scenarioBean = (Scenario) context.getBean(beanName);
            scenarioBean.run();
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}
