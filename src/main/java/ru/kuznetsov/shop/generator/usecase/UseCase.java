package ru.kuznetsov.shop.generator.usecase;

import org.springframework.http.HttpMethod;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

public interface UseCase<T> {

    String getUri();

    HttpMethod getMethod();

    Map<String, ?> getQueryParams();

    Map<String, List<String>> getHeaders();

    Object getBody();

    default Class<T> getReturnTypeClass(){
        return ((Class) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0]);
    }
}
