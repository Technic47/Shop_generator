package ru.kuznetsov.shop.generator.usecase.entity.address;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.AddressDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.ADDRESS_URI;

public class CreateAddressBatchUseCase implements UseCase<AddressDto> {

    private final String token;
    private final AddressDto[] addressList;

    public CreateAddressBatchUseCase(String token, AddressDto[] addressList) {
        this.token = token;
        this.addressList = addressList;
    }

    @Override
    public String getUri() {
        return ADDRESS_URI + "/batch";
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
        return addressList;
    }
}
