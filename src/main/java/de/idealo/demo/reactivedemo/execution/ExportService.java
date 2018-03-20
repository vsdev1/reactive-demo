package de.idealo.demo.reactivedemo.execution;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import de.idealo.demo.reactivedemo.iternal.api.ExportRequest;

@Service
@Slf4j
public class ExportService {

    private static final int THREAD_COUNT = 1;

    private static final String THREAD_POOL_NAME = "post-export";
    private static final String EXTERNAL_FAKE_POST_RESOURCE = "https://jsonplaceholder.typicode.com/posts/";
    private static final String INTERNAL_FAKE_POST_RESOURCE = "http://localhost:8080/exports";

    private final WebClient webClient = WebClient.create(createFakeResourceRequest(true));

    public Mono<String> export(Collection<MappedOffer> mappedOffers) {
        return webClient.post()
                .body(BodyInserters.fromObject(new ExportRequest(mappedOffers.size())))
                .retrieve()
                .bodyToMono(String.class);
    }

    private String createFakeResourceRequest(boolean external) {
        return external ? EXTERNAL_FAKE_POST_RESOURCE : INTERNAL_FAKE_POST_RESOURCE;
    }
}
