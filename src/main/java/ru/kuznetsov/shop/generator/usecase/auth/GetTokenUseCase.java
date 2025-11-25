package ru.kuznetsov.shop.generator.usecase.auth;

import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.auth.LoginPasswordDto;
import ru.kuznetsov.shop.represent.dto.auth.TokenDto;

import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.AUTH_URI;

public class GetTokenUseCase implements UseCase<TokenDto> {

    private final String login;
    private final String password;

    public GetTokenUseCase(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public String getUri() {
        return AUTH_URI;
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
        return Map.of();
    }

    @Override
    public Object getBody() {
        return new LoginPasswordDto(login, password);
    }
}
