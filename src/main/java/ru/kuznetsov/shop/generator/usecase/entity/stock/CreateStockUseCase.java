package ru.kuznetsov.shop.generator.usecase.entity.stock;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.STOCK_URI;

public class CreateStockUseCase implements UseCase<StockDto> {

    private final String token;
    private final StockDto stock;

    public CreateStockUseCase(String token, StockDto stock) {
        this.token = token;
        this.stock = stock;
    }

    @Override
    public String getUri() {
        return STOCK_URI;
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
        return stock;
    }
}
