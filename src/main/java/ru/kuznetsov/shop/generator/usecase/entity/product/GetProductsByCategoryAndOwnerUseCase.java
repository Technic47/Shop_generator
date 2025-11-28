package ru.kuznetsov.shop.generator.usecase.entity.product;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.ProductDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.kuznetsov.shop.generator.common.ConstValues.PRODUCT_URI;

public class GetProductsByCategoryAndOwnerUseCase implements UseCase<ProductDto> {

    private final String token;
    private final UUID ownerId;
    private final Long categoryId;

    public GetProductsByCategoryAndOwnerUseCase(String token, UUID ownerId, Long categoryId) {
        this.token = token;
        this.ownerId = ownerId;
        this.categoryId = categoryId;
    }

    @Override
    public String getUri() {
        return PRODUCT_URI;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, ?> getQueryParams() {
        return Map.of(
                "ownerId", ownerId,
                "categoryId", categoryId
        );
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
