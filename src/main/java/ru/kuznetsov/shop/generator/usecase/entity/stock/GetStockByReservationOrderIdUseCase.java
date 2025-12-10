package ru.kuznetsov.shop.generator.usecase.entity.stock;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ru.kuznetsov.shop.generator.usecase.UseCase;
import ru.kuznetsov.shop.represent.dto.StockDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.kuznetsov.shop.generator.common.ConstValues.STOCK_URI;

public class GetStockByReservationOrderIdUseCase implements UseCase<StockDto> {

    private final String token;
    private final Long reservationOrderId;

    public GetStockByReservationOrderIdUseCase(String token, Long reservationOrderId) {
        this.token = token;
        this.reservationOrderId = reservationOrderId;
    }

    @Override
    public String getUri() {
        return STOCK_URI + "/reservation";
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.GET;
    }

    @Override
    public Map<String, ?> getQueryParams() {
        Map<String, Object> queryParams = new HashMap<>(Collections.emptyMap());

        if (reservationOrderId != null) queryParams.put("reservationOrderId", reservationOrderId);

        return queryParams;
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
