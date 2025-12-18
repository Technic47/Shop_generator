package ru.kuznetsov.shop.generator.usecase.notification;

import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.kuznetsov.shop.generator.common.ConstValues.NOTIFICATION_URI;

public class DeleteNotificationUseCase implements UseCase<Boolean> {

    private final UUID notificationId;

    public DeleteNotificationUseCase(UUID notificationId) {
        this.notificationId = notificationId;
    }

    @Override
    public String getUri() {
        return NOTIFICATION_URI + "/" + notificationId.toString();
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
        return Map.of();
    }

    @Override
    public Object getBody() {
        return null;
    }
}
