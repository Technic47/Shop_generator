package ru.kuznetsov.shop.generator.usecase.entity.order.bucket;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.order.BucketItemDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.ORDER_BUCKET_URI;

public class BucketsByOrderIdUseCase implements UseCase<BucketItemDto> {

    private final String token;
    private final Long orderId;

    public BucketsByOrderIdUseCase(String token, @NonNull Long orderId) {
        this.token = token;
        this.orderId = orderId;
    }

    @Override
    public String getUri() {
        return ORDER_BUCKET_URI;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, ?> getQueryParams() {
        Map<String, Object> queryParams = new HashMap<>(Collections.emptyMap());

        queryParams.put("orderId", orderId);

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
