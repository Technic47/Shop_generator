package ru.kuznetsov.shop.generator.scenario.seller.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.scenario.AbstractScenario;
import ru.kuznetsov.shop.generator.service.GateUseCaseService;
import ru.kuznetsov.shop.generator.service.PaymentUseCaseService;
import ru.kuznetsov.shop.generator.usecase.entity.order.OrderByStatusUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.order.OrderPaidUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.order.status.SaveOrderStatusUseCase;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;
import ru.kuznetsov.shop.represent.dto.auth.UserDto;
import ru.kuznetsov.shop.represent.dto.order.OrderDto;
import ru.kuznetsov.shop.represent.dto.order.OrderStatusDto;
import ru.kuznetsov.shop.represent.enums.OrderStatusType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.SELLER_LOGIN;
import static ru.kuznetsov.shop.generator.common.ConstValues.SELLER_PASSWORD;
import static ru.kuznetsov.shop.represent.common.GlobalConst.DATE_FORMAT;
import static ru.kuznetsov.shop.represent.enums.OrderStatusType.ERROR;

@Component
public class ConfirmOrderPaymentScenario extends AbstractScenario {

    private final PaymentUseCaseService paymentUseCaseService;

    Logger logger = LoggerFactory.getLogger(ConfirmOrderPaymentScenario.class);

    protected ConfirmOrderPaymentScenario(GateUseCaseService gateUseCaseService, PaymentUseCaseService paymentUseCaseService) {
        super(gateUseCaseService);
        this.paymentUseCaseService = paymentUseCaseService;
    }

    @Override
    public void run(Map<String, String> parameters) {
        logger.info("Start ConfirmOrderPaymentScenario");

        TokenDto token = getToken(parameters, SELLER_LOGIN, SELLER_PASSWORD);
        String tokenString = token.getToken();

        UserDto userDto = getUserInfo(tokenString);

        logger.info("Getting orders with status AWAIT_PAYMENT");
        LocalDateTime dateAfter = LocalDateTime.now().minusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        List<Long> orderIdList = runUseCaseWithReturn(
                new OrderByStatusUseCase(
                        tokenString,
                        null,
                        dateAfter.format(formatter),
                        null,
                        OrderStatusType.AWAIT_PAYMENT,
                        OrderStatusType.SHIPPED)
        )
                .stream()
                .map(OrderDto::getId)
                .distinct()
                .toList();
        logger.info("Orders received: {}", orderIdList);

        logger.info("Sending payment success for orders with status AWAIT_PAYMENT");
        orderIdList.forEach(orderId -> {
            try {
                logger.info("Sending payment success for order: {}", orderId);
                paymentUseCaseService.runUseCase(new OrderPaidUseCase(orderId));
            } catch (Exception e) {
                logger.error(e.getMessage());

                OrderStatusDto orderStatus = createOrderStatus(
                        ERROR,
                        userDto.getId().toString(),
                        "Sending 'order payed' failed",
                        orderId
                );
                runUseCase(new SaveOrderStatusUseCase(tokenString, orderStatus));
            }
        });

        logger.info("Finish ConfirmOrderPaymentScenario");
    }

    private OrderStatusDto createOrderStatus(OrderStatusType status, String statusChangerId, String comment, Long orderId) {
        OrderStatusDto orderStatusDto = new OrderStatusDto();
        orderStatusDto.setStatus(status);
        orderStatusDto.setStatusChangerId(statusChangerId);
        orderStatusDto.setComment(comment);
        orderStatusDto.setOrderId(orderId);

        return orderStatusDto;
    }
}
