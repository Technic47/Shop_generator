package ru.kuznetsov.shop.generator.usecase.entity.order.status;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.order.OrderStatusDto;
import ru.kuznetsov.shop.represent.enums.OrderStatusType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.ORDER_STATUS_URI;

public class GetOrdersByStatusUseCase implements UseCase<OrderStatusDto> {

    private final String token;
    private final OrderStatusType status;
    private final String dateTime;
    private final String direction;
    private final Integer limit;

    public GetOrdersByStatusUseCase(String token, OrderStatusType status, String dateTime, String direction, Integer limit) {
        this.token = token;
        this.status = status;
        this.dateTime = dateTime;
        this.direction = direction;
        this.limit = limit;
    }

    @Override
    public String getUri() {
        return ORDER_STATUS_URI + "/status";
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, ?> getQueryParams() {
        Map<String, Object> queryParams = new HashMap<>(Collections.emptyMap());

        if (status != null) queryParams.put("status", status);
        if (dateTime != null) queryParams.put("dateTime", dateTime);
        if (direction != null) queryParams.put("direction", direction);
        if (limit != null) queryParams.put("limit", limit);

        return queryParams;
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return Map.of(HttpHeaders.AUTHORIZATION, Collections.singletonList(token));
    }

    @Override
    public Object getBody() {
        return null;
    }
}
