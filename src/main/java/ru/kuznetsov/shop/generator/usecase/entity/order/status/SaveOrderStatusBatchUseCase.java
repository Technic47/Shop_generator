package ru.kuznetsov.shop.generator.usecase.entity.order.status;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.order.OrderStatusDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.ORDER_STATUS_URI;

public class SaveOrderStatusBatchUseCase implements UseCase<OrderStatusDto> {

    private final String token;
    private final OrderStatusDto[] orderStatusList;

    public SaveOrderStatusBatchUseCase(String token, OrderStatusDto[] orderStatusList) {
        this.token = token;
        this.orderStatusList = orderStatusList;
    }

    @Override
    public String getUri() {
        return ORDER_STATUS_URI + "/batch";
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, ?> getQueryParams() {
        return Map.of();
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return Map.of(HttpHeaders.AUTHORIZATION, Collections.singletonList(token));
    }

    @Override
    public Object getBody() {
        return orderStatusList;
    }
}
