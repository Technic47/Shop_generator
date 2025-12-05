package ru.kuznetsov.shop.generator.usecase.entity.product;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.ProductDto;

import java.util.*;

import static ru.kuznetsov.shop.generator.common.ConstValues.PRODUCT_URI;

public class GetProductsByCategoryOrOwnerUseCase implements UseCase<ProductDto> {

    private final String token;
    private final UUID ownerId;
    private final Long categoryId;

    public GetProductsByCategoryOrOwnerUseCase(String token, UUID ownerId, Long categoryId) {
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
        return HttpMethod.GET;
    }

    @Override
    public Map<String, ?> getQueryParams() {
        Map<String, Object> queryParams = new HashMap<>(Collections.emptyMap());

        if (ownerId != null) queryParams.put("ownerId", ownerId);
        if (categoryId != null) queryParams.put("categoryId", categoryId);

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
