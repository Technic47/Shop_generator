package ru.kuznetsov.shop.generator.usecase.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.auth.UserDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.AUTH_URI;

public class GetUserInfoUseCase implements UseCase<UserDto> {

    private final String token;

    public GetUserInfoUseCase(String token) {
        this.token = token;
    }

    @Override
    public String getUri() {
        return AUTH_URI + "/userInfo";
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
        return null;
    }
}
