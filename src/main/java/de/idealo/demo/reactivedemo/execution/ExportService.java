package de.idealo.demo.reactivedemo.execution;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import de.idealo.demo.reactivedemo.config.ServicesProperties;
import de.idealo.demo.reactivedemo.iternal.api.ExportRequest;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExportService {

    private static final String EXTERNAL_FAKE_POST_RESOURCE = "https://jsonplaceholder.typicode.com/posts/";
    private static final String INTERNAL_FAKE_POST_RESOURCE = "http://localhost:8080/exports";

    private final ServicesProperties servicesProperties;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.create(createFakeResourceRequest());
    }

    public Mono<String> export(Collection<MappedOffer> mappedOffers) {
        return webClient.post()
                .body(BodyInserters.fromObject(new ExportRequest(mappedOffers.size())))
                .retrieve()
                .bodyToMono(String.class);
    }

    private String createFakeResourceRequest() {
        return servicesProperties.getExportService() != null && servicesProperties.getExportService().getExternalAPI()
                ? EXTERNAL_FAKE_POST_RESOURCE : INTERNAL_FAKE_POST_RESOURCE;
    }
}
