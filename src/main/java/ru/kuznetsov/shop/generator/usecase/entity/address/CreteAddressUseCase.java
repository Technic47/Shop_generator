package ru.kuznetsov.shop.generator.usecase.entity.address;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.AddressDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.ADDRESS_URI;

public class CreteAddressUseCase implements UseCase<AddressDto> {

    private final String token;
    private final AddressDto address;

    public CreteAddressUseCase(String token, AddressDto address) {
        this.token = token;
        this.address = address;
    }

    @Override
    public String getUri() {
        return ADDRESS_URI;
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
        return address;
    }
}
