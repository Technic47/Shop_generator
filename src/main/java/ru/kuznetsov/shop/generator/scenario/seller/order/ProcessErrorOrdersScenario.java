package ru.kuznetsov.shop.generator.scenario.seller.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kuznetsov.shop.generator.scenario.AbstractScenario;
import ru.kuznetsov.shop.generator.service.GateUseCaseService;
import ru.kuznetsov.shop.generator.usecase.entity.order.OrderByIdUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.order.status.DeleteOrderStatusUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.order.status.GetOrdersByStatusUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.order.status.SaveOrderStatusUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.stock.CreateStockUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.stock.GetStockByReservationOrderIdUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.stock.GetStockUseCase;
import ru.kuznetsov.shop.generator.usecase.entity.stock.UpdateStockUseCase;
import ru.kuznetsov.shop.parameter.service.ParameterService;
import ru.kuznetsov.shop.represent.dto.StockDto;
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
public class ProcessErrorOrdersScenario extends AbstractScenario {

    Logger logger = LoggerFactory.getLogger(ProcessErrorOrdersScenario.class);

    protected ProcessErrorOrdersScenario(GateUseCaseService gateUseCaseService, ParameterService parameterService) {
        super(gateUseCaseService, parameterService);
    }

    @Override
    public void run(Map<String, String> parameters) {
        logger.info("Start ProcessErrorOrdersScenario");

        TokenDto token = getToken(parameters, SELLER_LOGIN, SELLER_PASSWORD);
        String tokenString = token.getToken();

        UserDto userDto = getUserInfo(tokenString);

        logger.debug("Getting Order ids with status ERROR");
        LocalDateTime dateAfter = LocalDateTime.now().minusDays(2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        List<OrderStatusDto> errorOrdersIdList = runUseCaseWithReturn(new GetOrdersByStatusUseCase(
                token.getToken(),
                OrderStatusType.ERROR,
                dateAfter.format(formatter),
                "after",
                20
        ));

        errorOrdersIdList.forEach(statusDto -> {
            updateStockReservation(tokenString, statusDto.getOrderId(), userDto.getId().toString());
            runUseCaseWithReturn(new DeleteOrderStatusUseCase(tokenString, statusDto.getId()));
        });
    }

    private void updateStockReservation(String tokenString, Long orderId, String userId) {
        logger.info("Update Stock Reservation for order id {}", orderId);

        OrderDto order = runUseCaseWithReturn(new OrderByIdUseCase(tokenString, orderId)).get(0);

        order.getBucket().forEach(bucket -> {
            Long productId = bucket.getProductId();
            //Заказанное количество
            int bucketAmount = bucket.getAmount();

            List<StockDto> reservedStockForOrder = runUseCaseWithReturn(new GetStockByReservationOrderIdUseCase(tokenString, orderId));

            //Зарезервированное количество
            int reservedAmount = reservedStockForOrder.stream()
                    .filter(stock -> stock.getProductId().equals(productId))
                    .mapToInt(StockDto::getAmount)
                    .sum();

            if (reservedAmount != bucketAmount) {
                //Ошибка резервирования. Слишком много.
                if (reservedAmount > bucketAmount) {
                    OrderStatusDto errorStatus = createOrderStatus(
                            ERROR,
                            userId,
                            "Reserved amount is greater than ordered for product " + productId + ". Setting status Error.",
                            orderId
                    );
                    runUseCase(new SaveOrderStatusUseCase(tokenString, errorStatus));
                } else {

                    List<StockDto> stockDtos = runUseCaseWithReturn(new GetStockUseCase(
                            tokenString,
                            null,
                            productId
                    ));
                    //Отсутствует товар на складе
                    if (stockDtos.isEmpty()) {
                        OrderStatusDto errorStatus = createOrderStatus(
                                ERROR,
                                userId,
                                "No more stock for product " + productId + ". Setting status Error.",
                                orderId
                        );
                        runUseCase(new SaveOrderStatusUseCase(tokenString, errorStatus));
                    } else {
                        int difference = bucketAmount - reservedAmount;

                        for (StockDto stock : stockDtos) {
                            Integer stockAmount = stock.getAmount();

                            if (stockAmount >= difference) {
                                if (stockAmount == difference) {
                                    updateStockToReserved(stock, orderId, tokenString);
                                } else {
                                    stock.setAmount(stockAmount - difference);
                                    runUseCase(new UpdateStockUseCase(tokenString, stock));

                                    createStock(stock, difference, orderId, tokenString);
                                }
                                break;
                            } else {
                                updateStockToReserved(stock, orderId, tokenString);
                                difference = difference - stockAmount;
                            }
                        }
                    }
                }
            }
        });
    }

    private OrderStatusDto createOrderStatus(OrderStatusType status, String statusChangerId, String comment, Long orderId) {
        OrderStatusDto orderStatusDto = new OrderStatusDto();
        orderStatusDto.setStatus(status);
        orderStatusDto.setStatusChangerId(statusChangerId);
        orderStatusDto.setComment(comment);
        orderStatusDto.setOrderId(orderId);

        return orderStatusDto;
    }

    private void updateStockToReserved(StockDto stock, Long orderId, String tokenString) {
        stock.setIsReserved(true);
        stock.setReservationOrderId(orderId);
        runUseCase(new UpdateStockUseCase(tokenString, stock));
    }

    private void createStock(StockDto stock, int amount, Long orderId, String tokenString) {
        StockDto newStock = new StockDto();
        newStock.setProductId(stock.getProductId());
        newStock.setProductName(stock.getProductName());
        newStock.setStore(stock.getStore());
        newStock.setStoreAddress(stock.getStoreAddress());
        newStock.setAmount(amount);
        newStock.setIsReserved(true);
        newStock.setReservationOrderId(orderId);

        runUseCase(new CreateStockUseCase(tokenString, newStock));
    }
}
