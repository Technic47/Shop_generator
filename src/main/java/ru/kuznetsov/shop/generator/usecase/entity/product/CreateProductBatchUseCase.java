package ru.kuznetsov.shop.generator.usecase.entity.product;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.ProductDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.PRODUCT_URI;

public class CreateProductBatchUseCase implements UseCase<ProductDto> {

    private final String token;
    private final ProductDto[] productList;

    public CreateProductBatchUseCase(String token, ProductDto[] productList) {
        this.token = token;
        this.productList = productList;
    }

    @Override
    public String getUri() {
        return PRODUCT_URI + "/batch";
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
        return productList;
    }
}
