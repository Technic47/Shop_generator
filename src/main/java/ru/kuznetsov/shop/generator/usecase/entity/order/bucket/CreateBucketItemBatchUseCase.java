package ru.kuznetsov.shop.generator.usecase.entity.order.bucket;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.order.BucketItemDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.ORDER_BUCKET_URI;

public class CreateBucketItemBatchUseCase implements UseCase<BucketItemDto> {

    private final String token;
    private final BucketItemDto[] bucketItemList;

    public CreateBucketItemBatchUseCase(String token, BucketItemDto[] bucketItemList) {
        this.token = token;
        this.bucketItemList = bucketItemList;
    }

    @Override
    public String getUri() {
        return ORDER_BUCKET_URI + "/batch";
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
        return bucketItemList;
    }
}
