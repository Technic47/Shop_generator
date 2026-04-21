package ru.kuznetsov.shop.generator.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import ru.kuznetsov.shop.generator.exceptions.ServiceException;
import ru.kuznetsov.shop.generator.usecase.UseCase;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class WebClientConnector {

    @Value("${web.client.retry}")
    private long RETRY;
    @Value("${web.client.max-attempts}")
    private int MAX_ATTEMPTS;

    private final WebClient webClient;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public WebClientConnector(WebClient webClient) {
        this.webClient = webClient;
    }

    public <T> List<T> sendRequest(UseCase<T> useCase) {
        return sendRequest(
                useCase.getMethod(),
                useCase.getUri(),
                useCase.getQueryParams(),
                useCase.getHeaders(),
                useCase.getBody(),
                useCase.getReturnTypeClass()
        );
    }

    public <T> List<T> sendRequest(HttpMethod method, String uri, Map<String, ?> queryParams, Map<String, List<String>> headersMap, Object body, Class<T> clazz) {
        Mono<List<T>> mono = webClient.method(method)
                .uri(uriBuilder -> {
                    uriBuilder.path(uri);
                    if (queryParams != null) {
                        for (Map.Entry<String, ?> entry : queryParams.entrySet()) {
                            uriBuilder.queryParam(entry.getKey(), entry.getValue());
                        }
                    }
                    return uriBuilder.build();
                })
                .headers(headers -> headers.putAll(headersMap))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body == null ? "" : body)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifModifiedSince(ZonedDateTime.now())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    logger.error(formClientErrorMessage(response, method, uri, queryParams, headersMap, clazz));
                    return response.bodyToMono(String.class).map(RuntimeException::new);
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    logger.error(formServerErrorMessage(response, method, uri, queryParams, headersMap, clazz));
                    return Mono.empty();
                })
                .bodyToFlux(clazz)
                .retryWhen(Retry.backoff(MAX_ATTEMPTS, Duration.ofMillis(RETRY))
                        .jitter(0.75)
                        .doBeforeRetry(x -> System.out.println("Повторная попытка отправки " + x.totalRetries()))
                        .filter(throwable -> throwable instanceof ServiceException
                                || throwable instanceof WebClientRequestException)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ServiceException("Повторные попытки исчерпаны. Внешний сервис недоступен.",
                                    HttpStatus.SERVICE_UNAVAILABLE.value());
                        }))
                .onErrorResume(ServiceException.class, err -> {
                    System.out.println("Код ошибки: " + err.getStatusCode());
                    err.printStackTrace();
                    return Mono.empty();
                })
                .collectList();
        return mono.block();
    }

    private String formServerErrorMessage(ClientResponse response, HttpMethod method, String uri, Map<String, ?> queryParams, Map<String, List<String>> headersMap, Object body) {
        return formErrorMessage(
                "Ошибка сервера",
                response,
                method,
                uri,
                queryParams,
                headersMap,
                body
        );
    }

    private String formClientErrorMessage(ClientResponse response, HttpMethod method, String uri, Map<String, ?> queryParams, Map<String, List<String>> headersMap, Object body) {
        return formErrorMessage(
                "Ошибка клиента",
                response,
                method,
                uri,
                queryParams,
                headersMap,
                body
        );
    }

    private String formErrorMessage(String message, ClientResponse response, HttpMethod method, String uri, Map<String, ?> queryParams, Map<String, List<String>> headersMap, Object body) {
        StringBuilder builder = new StringBuilder();
        builder.append(message).append(" ").append(response.statusCode()).append(" ")
                .append("Method: ").append(method.toString()).append(" ").append("Uri: ").append(uri).append(" ");

        if (queryParams != null && !queryParams.isEmpty()) {
            builder.append("Params: ");
            for (String key : queryParams.keySet()) {
                builder.append(key).append(": ").append(queryParams.get(key));
            }
            builder.append(" ");
        }

        if (headersMap != null && !headersMap.isEmpty()) {
            builder.append("Headers: ");
            for (String key : headersMap.keySet()) {
                builder.append(key).append(": ");
                builder.append(key.contains("Authorization") ?
                        "***" :
                        headersMap.get(key));
            }
            builder.append(" ");
        }

        if (body != null) {
            builder.append("Body: ").append(body);
        }

        return builder.toString();
    }
}
