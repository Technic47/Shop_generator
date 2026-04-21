package ru.kuznetsov.shop.generator.usecase.auth;

import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.auth.UserRepresentationDto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.AUTH_URI;

public class CreateUserUseCase implements UseCase<String> {

    private final UserRepresentationDto userDto;

    public CreateUserUseCase(UserRepresentationDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public String getUri() {
        return AUTH_URI + "/user";
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Map<String, ?> getQueryParams() {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, List<String>> getHeaders() {
        return Collections.emptyMap();
    }

    @Override
    public Object getBody() {
        return userDto;
    }
}
