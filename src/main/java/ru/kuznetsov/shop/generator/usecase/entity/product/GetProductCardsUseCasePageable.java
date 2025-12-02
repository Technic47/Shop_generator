package ru.kuznetsov.shop.generator.usecase.entity.product;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.util.ProductCardPage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.kuznetsov.shop.generator.common.ConstValues.PRODUCT_URI;

public class GetProductCardsUseCasePageable implements UseCase<ProductCardPage> {

    private final String token;
    private final UUID ownerId;
    private final Long categoryId;
    private final Integer pageNumber;
    private final Integer pageSize;
    private final String sortBy;
    private final String order;

    public GetProductCardsUseCasePageable(String token, UUID ownerId, Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String order) {
        this.token = token;
        this.ownerId = ownerId;
        this.categoryId = categoryId;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.order = order;
    }

    @Override
    public String getUri() {
        return PRODUCT_URI + "/card/page";
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
        if (pageNumber != null) queryParams.put("pageNumber", pageNumber);
        if (pageSize != null) queryParams.put("pageSize", pageSize);
        if (sortBy != null) queryParams.put("sortBy", sortBy);
        if (order != null) queryParams.put("order", order);

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
