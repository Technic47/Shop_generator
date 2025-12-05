package ru.kuznetsov.shop.generator.usecase.entity.stock;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.STOCK_URI;

public class GetStockUseCase implements UseCase<StockDto> {

    private final String token;
    private final Long storeId;
    private final Long productId;

    public GetStockUseCase(String token, Long storeId, Long productId) {
        this.token = token;
        this.storeId = storeId;
        this.productId = productId;
    }

    @Override
    public String getUri() {
        return STOCK_URI;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, ?> getQueryParams() {
        Map<String, Object> queryParams = new HashMap<>(Collections.emptyMap());

        if (storeId != null) queryParams.put("storeId", storeId);
        if (productId != null) queryParams.put("productId", productId);

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
