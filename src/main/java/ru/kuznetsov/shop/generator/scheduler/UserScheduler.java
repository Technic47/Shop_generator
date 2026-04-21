package ru.kuznetsov.shop.generator.scheduler;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.service.MockUserRecordService;
import ru.kuznetsov.shop.parameter.service.ParameterService;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;

import static ru.kuznetsov.shop.generator.common.ConstValues.PARAMETER_LOGIN;
import static ru.kuznetsov.shop.generator.common.ConstValues.PARAMETER_PASSWORD;

@Component
public class UserScheduler extends AbstractScheduler {

    private final static String NEW_MOCK_ORDER_PROBABILITY = "NEW_MOCK_ORDER_PROBABILITY";
    private final static String NEW_MOCK_USER_PROBABILITY = "NEW_MOCK_USER_PROBABILITY";

    private final MockUserRecordService userRecordService;

    protected UserScheduler(ApplicationContext context, ExecutorService executor, ParameterService parameterService, MockUserRecordService userRecordService) {
        super(context, executor, parameterService);
        this.userRecordService = userRecordService;
    }


    @Scheduled(cron = "0 */1 * 1 * *")
    public void scheduleCreateOrderScenario() {
        int newMockOrderProbability = Integer.parseInt(
                parameterService.getParameterValueStringOrSaveDefault(
                        NEW_MOCK_ORDER_PROBABILITY,
                        "75",
                        "Вероятность создания нового демо-заказа"
                ));

        userRecordService.getAll()
                .forEach(mockUser -> {
                    int newOrderProbability = new Random().nextInt(100);

                    if (newOrderProbability <= newMockOrderProbability) {
                        var parameters = Map.of(
                                PARAMETER_LOGIN, mockUser.getUserName(),
                                PARAMETER_PASSWORD, mockUser.getPassWord()
                        );

                        executor.submit(() -> processScenario("CreateOrderScenario", parameters));
                    }
                });
    }

    @Scheduled(cron = "0 */30 * 1 * *")
    public void scheduleCreateNewUserScenario() {
        int newMockOrderProbability = Integer.parseInt(
                parameterService.getParameterValueStringOrSaveDefault(
                        NEW_MOCK_USER_PROBABILITY,
                        "50",
                        "Вероятность создания нового демо-пользователя"
                ));

        if (new Random().nextInt(100) < newMockOrderProbability) {
            executor.submit(() -> processScenario("CreateOrderScenario", Collections.emptyMap()));
        }
    }
}
