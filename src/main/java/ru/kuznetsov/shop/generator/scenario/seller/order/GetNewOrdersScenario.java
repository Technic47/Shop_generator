package ru.kuznetsov.shop.generator.scenario.seller.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.scenario.AbstractScenario;
import ru.kuznetsov.shop.generator.scenario.seller.product.ProductStockUpdateScenario;
import ru.kuznetsov.shop.generator.service.GateUseCaseService;
import ru.kuznetsov.shop.generator.service.NotificationUseCaseService;
import ru.kuznetsov.shop.generator.usecase.auth.GetUserInfoUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.order.status.SaveOrderStatusUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.stock.GetStockByReservationOrderIdUseCase;
import ru.kuznetsov.shop.generator.usecase.notification.DeleteNotificationUseCase;
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

    protected final NotificationUseCaseService notificationUseCaseService;

    Logger logger = LoggerFactory.getLogger(ProductStockUpdateScenario.class);

    protected GetNewOrdersScenario(GateUseCaseService gateUseCaseService, NotificationUseCaseService notificationUseCaseService) {
        super(gateUseCaseService);
        this.notificationUseCaseService = notificationUseCaseService;
    }

    @Override
    public void run() {
        logger.info("Start GetNewOrdersScenario");

        TokenDto token = getToken(SELLER_LOGIN, SELLER_PASSWORD);
        String tokenString = token.getToken();

        logger.info("Getting user");
        UserDto userDto = runUseCaseWithReturn(new GetUserInfoUseCase(tokenString)).get(0);
        logger.info("UserInfo: {}", userDto);

        logger.info("Getting new order notifications");
        List<SellerNotificationDto> notifications = notificationUseCaseService.runUseCase(new GetNotificationByOwnerIdUseCase(userDto.getId().toString()));
        logger.info("New notifications: {}", notifications.size());

        for (SellerNotificationDto notification : notifications) {
            Long orderId = notification.getOrderId();

            List<Boolean> list = notification.getProducts().stream()
                    .map(bucket -> stockCheck(tokenString, orderId, bucket))
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

                notificationUseCaseService.runUseCase(new DeleteNotificationUseCase(notification.getId()));
            }

            runUseCase(new SaveOrderStatusUseCase(tokenString, orderStatus));
        }

        logger.info("Finished GetNewOrdersScenario");
    }

    private boolean stockCheck(String tokenString, Long orderId, BucketItemDto bucket) {
        boolean stockCheck = true;
        boolean reservationCheck = true;
        boolean amountCheck = true;

        Long bucketId = bucket.getId();
        Long productId = bucket.getProductId();
        Integer bucketAmount = bucket.getAmount();
        StringBuilder builder = new StringBuilder();

        logger.info("Start stock checking for bucket: {}", bucketId);

        List<StockDto> stockList = runUseCaseWithReturn(new GetStockByReservationOrderIdUseCase(tokenString, orderId)).stream()
                .filter(stock -> stock.getProductId().equals(productId))
                .toList();

        //Проверка на наличие запаса на складе
        if (stockList.isEmpty()) {
            stockCheck = false;
            logger.info("No stock found for productId: {}", bucketId);
            builder.append("No stock found for productId: ").append(bucketId);
        } else {

            reservationCheck = stockList.stream()
                    .filter(stock -> stock.getIsReserved().equals(false))
                    .toList()
                    .isEmpty();

            //Проверка на то, что везде проставлен резерв
            if (!reservationCheck) {
                builder.append(" ");
                logger.info("Stock found for orderId: {} is not correctly reserved for productId: {}", orderId, bucketId);
                builder.append("Stock found for orderId: ").append(orderId).append(" is not correctly reserved for productId: ").append(bucketId);
            }

            List<Integer> reservedAmountList = stockList.stream()
                    .map(StockDto::getAmount)
                    .toList();

            amountCheck = reservedAmountList.contains(bucketAmount);

            //Проверка на то, что зарезервировано достаточное количество
            if (!amountCheck) {
                builder.append(" ");
                logger.info("Stock amount reserved for orderId: {} is not fully reserved for productId: {}", orderId, bucketId);
                builder.append("Stock amount reserved for productId: ").append(bucketId).append(" is not fully reserved for productId: ").append(bucketId);
            }
        }
        boolean checkResult = stockCheck && reservationCheck && amountCheck;
        errorString = checkResult ? null : builder.toString();

        logger.info("Finished stock checking for bucket: {} with result: {}", bucketId, checkResult);
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
