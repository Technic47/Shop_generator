package ru.kuznetsov.shop.generator.usecase.entity.product_category;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.ProductCategoryDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.PRODUCT_CATEGORY_URI;

public class GetAllCategoriesUseCase implements UseCase<ProductCategoryDto> {

    private final String token;

    public GetAllCategoriesUseCase(String token) {
        this.token = token;
    }

    @Override
    public String getUri() {
        return PRODUCT_CATEGORY_URI;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
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
