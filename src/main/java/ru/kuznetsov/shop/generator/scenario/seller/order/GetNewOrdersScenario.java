package ru.kuznetsov.shop.generator.scenario.seller.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.scenario.AbstractScenario;
import ru.kuznetsov.shop.generator.scenario.seller.product.ProductStockUpdateScenario;
import ru.kuznetsov.shop.generator.service.UseCaseService;
import ru.kuznetsov.shop.generator.usecase.auth.GetUserInfoUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.order.status.SaveOrderStatusUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.stock.GetStockByReservationOrderIdUseCase;
import ru.kuznetsov.shop.generator.usecase.notification.GetNotificationByOwnerIdUseCase;
import ru.kuznetsov.shop.represent.dto.StockDto;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;
import ru.kuznetsov.shop.represent.dto.auth.UserDto;
import ru.kuznetsov.shop.represent.dto.order.BucketItemDto;
import ru.kuznetsov.shop.represent.dto.order.OrderStatusDto;
import ru.kuznetsov.shop.represent.dto.order.SellerNotificationDto;
import ru.kuznetsov.shop.represent.enums.OrderStatusType;

import java.util.List;

import static ru.kuznetsov.shop.generator.common.ConstValues.SELLER_LOGIN;
import static ru.kuznetsov.shop.generator.common.ConstValues.SELLER_PASSWORD;
import static ru.kuznetsov.shop.represent.enums.OrderStatusType.ERROR;
import static ru.kuznetsov.shop.represent.enums.OrderStatusType.FORMED;

@Component
public class GetNewOrdersScenario extends AbstractScenario {

    private String errorString;

    Logger logger = LoggerFactory.getLogger(ProductStockUpdateScenario.class);

    protected GetNewOrdersScenario(UseCaseService useCaseService) {
        super(useCaseService);
    }

    @Override
    public void run() {
        TokenDto token = getToken(SELLER_LOGIN, SELLER_PASSWORD);
        String tokenString = token.getToken();

        logger.info("Getting user");
        UserDto userDto = runUseCaseWithReturn(new GetUserInfoUseCase(tokenString)).get(0);
        logger.info("UserInfo: {}", userDto);

        logger.info("Getting new order notifications");
        List<SellerNotificationDto> notifications = runUseCaseWithReturn(new GetNotificationByOwnerIdUseCase(userDto.getId().toString()));
        logger.info("New notifications: {}", notifications.size());

        for (SellerNotificationDto notification : notifications) {
            Long orderId = notification.getOrderId();

            List<Boolean> list = notification.getProducts().stream()
                    .map(product -> stockCheck(tokenString, orderId, product))
                    .toList();

            OrderStatusDto orderStatus;

            if (list.contains(false)) {
                orderStatus = createOrderStatus(
                        ERROR,
                        userDto.getId().toString(),
                        errorString,
                        orderId
                );
            } else {
                orderStatus = createOrderStatus(
                        FORMED,
                        userDto.getId().toString(),
                        "Order ready",
                        orderId
                );
            }

            runUseCase(new SaveOrderStatusUseCase(tokenString, orderStatus));
        }
    }

    private boolean stockCheck(String tokenString, Long orderId, BucketItemDto product) {
        logger.info("Start stock checking for product: {}", product.getId());

        boolean stockCheck = true;
        boolean reservationCheck = true;
        boolean amountCheck = true;
        StringBuilder builder = new StringBuilder();
        List<StockDto> stockList = runUseCaseWithReturn(new GetStockByReservationOrderIdUseCase(tokenString, orderId));

        if (stockList.isEmpty()) {
            stockCheck = false;
            logger.info("No stock found for productId: {}", product.getId());
            builder.append("No stock found for productId: ").append(product.getId());
        } else {
            reservationCheck = stockList.stream()
                    .filter(stock -> stock.getIsReserved().equals(false))
                    .toList()
                    .isEmpty();

            if (!reservationCheck) {
                builder.append(" ");
                logger.info("Stock found for orderId: {} is not fully reserved for productId: {}", orderId, product.getId());
                builder.append("Stock found for orderId: ").append(orderId).append(" is not fully reserved for productId: ").append(product.getId());
            }

            int stockSum = stockList.stream()
                    .mapToInt(StockDto::getAmount)
                    .sum();

            amountCheck = product.getAmount().equals(stockSum);

            if (!amountCheck) {
                builder.append(" ");
                logger.info("Stock amount reserved for orderId: {} is not fully reserved for productId: {}", orderId, product.getId());
                builder.append("Stock amount reserved for productId: ").append(product.getId()).append(" is not fully reserved for productId: ").append(product.getId());
            }
        }
        boolean checkResult = stockCheck && reservationCheck && amountCheck;
        errorString = checkResult ? null : builder.toString();

        logger.info("Finished stock checking for product: {} with result: {}", product.getId(), checkResult);
        return checkResult;
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
