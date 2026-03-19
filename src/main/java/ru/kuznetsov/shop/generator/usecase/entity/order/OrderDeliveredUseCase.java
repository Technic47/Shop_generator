package ru.kuznetsov.shop.generator.usecase.entity.order;

import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.order.OrderStatusDto;

import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.SHIPMENT_URI;

public class OrderDeliveredUseCase implements UseCase<OrderStatusDto> {

    private final Long orderId;

    public OrderDeliveredUseCase(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String getUri() {
        return SHIPMENT_URI + "/delivered";
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, ?> getQueryParams() {
        return Map.of("orderId", orderId);
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return Map.of();
    }

    @Override
    public Object getBody() {
        return null;
    }
}
