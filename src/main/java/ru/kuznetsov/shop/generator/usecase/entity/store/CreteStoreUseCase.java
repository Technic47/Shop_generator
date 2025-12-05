package ru.kuznetsov.shop.generator.usecase.entity.store;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.StoreDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.STORE_URI;

public class CreteStoreUseCase implements UseCase<StoreDto> {

    private final String token;
    private final StoreDto storeDto;

    public CreteStoreUseCase(String token, StoreDto storeDto) {
        this.token = token;
        this.storeDto = storeDto;
    }

    @Override
    public String getUri() {
        return STORE_URI;
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
        return storeDto;
    }
}
