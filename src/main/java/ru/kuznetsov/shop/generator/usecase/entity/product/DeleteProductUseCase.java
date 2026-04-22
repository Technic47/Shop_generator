package ru.kuznetsov.shop.generator.usecase.entity.product;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.ProductDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.PRODUCT_URI;

public class DeleteProductUseCase implements UseCase<ProductDto> {

    private final String token;
    private final Long id;

    public DeleteProductUseCase(String token, Long id) {
        this.token = token;
        this.id = id;
    }

    @Override
    public String getUri() {
        return PRODUCT_URI + "/" + id;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.DELETE;
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
