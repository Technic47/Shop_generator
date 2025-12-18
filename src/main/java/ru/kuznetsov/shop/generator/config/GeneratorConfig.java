package ru.kuznetsov.shop.generator.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GeneratorConfig {

    @Value("${microservices.baseUrl}")
    private String baseUrl;

    private final static String GATE_PORT = "8080";
    private final static String NOTIFICATION_STAB_PORT = "8088";

    @Bean
    @Qualifier("gate")
    public WebClient getGateClient() {
        return getWebClient(GATE_PORT);
    }

    @Bean
    @Qualifier("notification")
    public WebClient getNotificationClient() {
        return getWebClient(NOTIFICATION_STAB_PORT);
    }

    private WebClient getWebClient(String port) {
        return WebClient.builder()
                .baseUrl(baseUrl + ":" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
