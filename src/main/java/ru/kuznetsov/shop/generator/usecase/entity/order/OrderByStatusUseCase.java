package ru.kuznetsov.shop.generator.usecase.entity.order;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.order.OrderDto;
import ru.kuznetsov.shop.represent.enums.OrderStatusType;

import java.util.*;

import static ru.kuznetsov.shop.generator.common.ConstValues.ORDER_URI;

public class OrderByStatusUseCase implements UseCase<OrderDto> {

    private final String token;
    private final UUID customerId;
    private final String dateAfter;
    private final String dateBefore;
    private final OrderStatusType hasStatus;
    private final OrderStatusType hasNotStatus;

    public OrderByStatusUseCase(String token,
                                UUID customerId,
                                String dateAfter,
                                String dateBefore,
                                @NonNull OrderStatusType hasStatus,
                                @NonNull OrderStatusType hasNotStatus) {
        this.token = token;
        this.customerId = customerId;
        this.dateAfter = dateAfter;
        this.dateBefore = dateBefore;
        this.hasStatus = hasStatus;
        this.hasNotStatus = hasNotStatus;
    }

    @Override
    public String getUri() {
        return ORDER_URI + "/hasStatus";
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, ?> getQueryParams() {
        Map<String, Object> queryParams = new HashMap<>(Collections.emptyMap());

        if (customerId != null) queryParams.put("customerId", customerId);
        if (dateAfter != null) queryParams.put("dateAfter", dateAfter);
        if (dateBefore != null) queryParams.put("dateBefore", dateBefore);
        queryParams.put("hasStatus", hasStatus);
        queryParams.put("hasNotStatus", hasNotStatus);

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
