package ru.kuznetsov.shop.generator.usecase.entity.stock;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.STOCK_URI;

public class CreateStockBatchUseCase implements UseCase<StockDto> {

    private final String token;
    private final StockDto[] stockList;

    public CreateStockBatchUseCase(String token, StockDto[] stockList) {
        this.token = token;
        this.stockList = stockList;
    }

    @Override
    public String getUri() {
        return STOCK_URI + "/batch";
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
        return stockList;
    }
}
