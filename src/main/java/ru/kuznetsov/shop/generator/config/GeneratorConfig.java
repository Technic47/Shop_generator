package ru.kuznetsov.shop.generator.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
@EnableJpaRepositories("ru.kuznetsov.shop.generator.repository")
@EntityScan("ru.kuznetsov.shop.generator.model")
public class GeneratorConfig {

    @Value("${microservices.baseUrl}")
    private String baseUrl;

    private final static String GATE_PORT = "8080";
    private final static String NOTIFICATION_STAB_PORT = "8088";
    private final static String PAYMENT_MODULE_PORT = "9087";
    private final static String SHIPMENT_MODULE_PORT = "9088";

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

    @Bean
    @Qualifier("payment")
    public WebClient getPaymentClient() {
        return getWebClient(PAYMENT_MODULE_PORT);
    }

    @Bean
    @Qualifier("shipment")
    public WebClient getShipmentClient() {
        return getWebClient(SHIPMENT_MODULE_PORT);
    }

    @Bean
    public ExecutorService getExecutorService() {
        return Executors.newFixedThreadPool(10);
    }

    private WebClient getWebClient(String port) {
        return WebClient.builder()
                .baseUrl(baseUrl + ":" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
