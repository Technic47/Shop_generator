package ru.kuznetsov.shop.generator.usecase.entity.order;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.order.OrderDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.ORDER_URI;

public class OrderByIdUseCase implements UseCase<OrderDto> {

    private final String token;
    private final Long orderId;

    public OrderByIdUseCase(String token, Long orderId) {
        this.token = token;
        this.orderId = orderId;
    }

    @Override
    public String getUri() {
        return ORDER_URI + "/" + orderId;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
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
        return null;
    }
}
