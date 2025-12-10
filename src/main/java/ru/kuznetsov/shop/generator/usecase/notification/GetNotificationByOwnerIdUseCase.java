package ru.kuznetsov.shop.generator.usecase.notification;

import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.order.SellerNotificationDto;

import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.NOTIFICATION_URI;

public class GetNotificationByOwnerIdUseCase implements UseCase<SellerNotificationDto> {

    private final String ownerId;

    public GetNotificationByOwnerIdUseCase(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String getUri() {
        return NOTIFICATION_URI + "/" + ownerId;
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
        return Map.of();
    }

    @Override
    public Object getBody() {
        return null;
    }
}
