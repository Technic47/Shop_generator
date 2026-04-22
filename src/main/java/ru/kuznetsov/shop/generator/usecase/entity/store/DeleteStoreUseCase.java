package ru.kuznetsov.shop.generator.usecase.entity.store;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.StoreDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.STORE_URI;

public class DeleteStoreUseCase implements UseCase<StoreDto> {

    private final String token;
    private final Long id;

    public DeleteStoreUseCase(String token, Long id) {
        this.token = token;
        this.id = id;
    }

    @Override
    public String getUri() {
        return STORE_URI + "/" + id;
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
