package ru.kuznetsov.shop.generator.scenario.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.model.MockUserRecord;
import ru.kuznetsov.shop.generator.scenario.AbstractScenario;
import ru.kuznetsov.shop.generator.service.GateUseCaseService;
import ru.kuznetsov.shop.generator.service.MockUserRecordService;
import ru.kuznetsov.shop.generator.usecase.auth.CreateUserUseCase;
import ru.kuznetsov.shop.parameter.service.ParameterService;
import ru.kuznetsov.shop.represent.dto.auth.UserRepresentationDto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Component
public class CreateUserScenario extends AbstractScenario {

    Logger logger = LoggerFactory.getLogger(CreateUserScenario.class);

    private final MockUserRecordService userService;

    protected CreateUserScenario(GateUseCaseService gateUseCaseService, ParameterService parameterService, MockUserRecordService userService) {
        super(gateUseCaseService, parameterService);
        this.userService = userService;
    }

    @Override
    public void run(Map<String, String> parameters) {
        logger.info("Start CreateUserScenario");

        int newUserNumber = new Random().nextInt(10000);
        String newUserUserName = "GeneratedUserName_" + newUserNumber;
        String newUserLogin = "Generated_" + newUserNumber;
        String newUserPassWord = "GeneratedPassword_" + newUserNumber;

        try {
            String newUserId = runUseCaseWithReturn(new CreateUserUseCase
                    (new UserRepresentationDto(
                            newUserUserName,
                            "GeneratedFirstName_" + newUserNumber,
                            "GeneratedLastName_" + newUserNumber,
                            "Generated_" + newUserNumber + "@mail.ru",
                            true,
                            true,
                            newUserLogin,
                            newUserPassWord
                    )))
                    .get(0);
            logger.info("New User with Id: {} saved to Keycloak", newUserId);

            if (newUserId != null && !newUserId.isBlank()) {
                userService.save(new MockUserRecord(
                        null,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        newUserUserName,
                        newUserLogin,
                        newUserPassWord
                ));
                logger.info("MockUser info saved to db for user with Id: {}", newUserId);
            } else {
                logger.info("Error while saving MockUser info for user with Id: {}", newUserId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error in CreateUserScenario", e);
        }

        logger.info("End CreateUserScenario");
    }
}
