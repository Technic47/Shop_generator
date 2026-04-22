package ru.kuznetsov.shop.generator.usecase.entity.order.bucket;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.order.BucketItemDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.ORDER_BUCKET_URI;

public class GetBucketItemsForUserUseCase implements UseCase<BucketItemDto> {

    private final String token;

    public GetBucketItemsForUserUseCase(String token) {
        this.token = token;
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
