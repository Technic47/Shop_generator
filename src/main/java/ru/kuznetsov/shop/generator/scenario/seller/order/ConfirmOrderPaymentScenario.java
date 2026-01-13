package ru.kuznetsov.shop.generator.scenario.seller.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.scenario.AbstractScenario;
import ru.kuznetsov.shop.generator.service.GateUseCaseService;
import ru.kuznetsov.shop.generator.service.PaymentUseCaseService;
import ru.kuznetsov.shop.generator.usecase.auth.GetUserInfoUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.order.OrderPaidUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.order.status.GetOrdersByStatusUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.order.status.SaveOrderStatusUseCase;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;
import ru.kuznetsov.shop.represent.dto.auth.UserDto;
import ru.kuznetsov.shop.represent.dto.order.OrderStatusDto;
import ru.kuznetsov.shop.represent.enums.OrderStatusType;

import java.util.List;

import static ru.kuznetsov.shop.generator.common.ConstValues.SELLER_LOGIN;
import static ru.kuznetsov.shop.generator.common.ConstValues.SELLER_PASSWORD;
import static ru.kuznetsov.shop.represent.enums.OrderStatusType.ERROR;

@Component
public class ConfirmOrderPaymentScenario extends AbstractScenario {

    protected final PaymentUseCaseService paymentUseCaseService;

    Logger logger = LoggerFactory.getLogger(ConfirmOrderPaymentScenario.class);

    protected ConfirmOrderPaymentScenario(GateUseCaseService gateUseCaseService, PaymentUseCaseService paymentUseCaseService) {
        super(gateUseCaseService);
        this.paymentUseCaseService = paymentUseCaseService;
    }

    @Override
    public void run() {
        logger.info("Start ConfirmOrderPaymentScenario");

        TokenDto token = getToken(SELLER_LOGIN, SELLER_PASSWORD);
        String tokenString = token.getToken();

        logger.info("Getting user");
        UserDto userDto = runUseCaseWithReturn(new GetUserInfoUseCase(tokenString)).get(0);
        logger.info("UserInfo: {}", userDto);

        logger.info("Getting orders with status AWAIT_PAYMENT");
        List<Long> orderIdList = runUseCaseWithReturn(new GetOrdersByStatusUseCase(tokenString, OrderStatusType.AWAIT_PAYMENT))
                .stream()
                .map(OrderStatusDto::getOrderId)
                .distinct()
                .toList();
        logger.info("Orders received: {}", orderIdList);

        logger.info("Sending payment success for orders with status AWAIT_PAYMENT");
        orderIdList.forEach(orderId -> {
            try {
                paymentUseCaseService.runUseCase(new OrderPaidUseCase(orderId));
                logger.info("Sending payment success for order: {}", orderId);
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
