package de.idealo.demo.reactivedemo.execution;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import de.idealo.demo.reactivedemo.config.ServicesProperties;

@Service
@Slf4j
@RequiredArgsConstructor
public class MetaDataService {

    private static final String EXTERNAL_FAKE_GET_RESOURCE = "https://jsonplaceholder.typicode.com/posts/1";
    private static final String INTERNAL_FAKE_GET_RESOURCE = "http://localhost:8080/meta-data";

    private final ServicesProperties servicesProperties;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder()
                .baseUrl(createFakeResourceRequest())
                .filter(logRequest())
                .build();
    }

    public Mono<String> getMetaData(String productTitle) {
        return webClient.get()
                .uri(builder -> builder.path("/").queryParam("productTitle", productTitle).build())
                .retrieve().bodyToMono(String.class);
    }

    private String createFakeResourceRequest() {
        return servicesProperties.getMetaDataService() != null && servicesProperties.getMetaDataService().getExternalAPI()
                ? EXTERNAL_FAKE_GET_RESOURCE : INTERNAL_FAKE_GET_RESOURCE;
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));

            return Mono.just(clientRequest);
        });
    }
}
