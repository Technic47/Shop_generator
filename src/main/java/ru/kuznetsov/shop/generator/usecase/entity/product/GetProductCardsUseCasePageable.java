package ru.kuznetsov.shop.generator.usecase.entity.product;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.util.ProductCardPage;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.kuznetsov.shop.generator.common.ConstValues.PRODUCT_URI;

public class GetProductCardsUseCasePageable implements UseCase<ProductCardPage> {

    private final String token;
    private final UUID ownerId;
    private final Long categoryId;
    private final Integer pageNum;
    private final Integer pageSize;
    private final String sortBy;
    private final String sortDirection;

    public GetProductCardsUseCasePageable(String token, UUID ownerId, Long categoryId, Integer pageNum, Integer pageSize, String sortBy, String sortDirection) {
        this.token = token;
        this.ownerId = ownerId;
        this.categoryId = categoryId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
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
        return Map.of(
                "ownerId", ownerId,
                "categoryId", categoryId,
                "pageNum", pageNum,
                "pageSize", pageSize,
                "sortBy", sortBy,
                "sortDirection", sortDirection
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
