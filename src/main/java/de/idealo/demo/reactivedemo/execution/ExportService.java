package de.idealo.demo.reactivedemo.execution;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Response;
import org.asynchttpclient.extras.rxjava2.RxHttpClient;
import org.springframework.stereotype.Service;

import io.reactivex.Maybe;
import lombok.extern.slf4j.Slf4j;

import de.idealo.demo.reactivedemo.config.ServicesProperties;

@Service
@Slf4j
public class ExportService {

    private static final String THREAD_POOL_NAME = "post-export";
    private static final String EXTERNAL_FAKE_POST_RESOURCE = "https://jsonplaceholder.typicode.com/posts/";
    private static final String INTERNAL_FAKE_POST_RESOURCE = "http://localhost:8080/exports";

    private final ServicesProperties servicesProperties;

    private final DefaultAsyncHttpClientConfig.Builder config = config().setThreadPoolName(THREAD_POOL_NAME);
    private final AsyncHttpClient asyncHttpClient = asyncHttpClient(config);
    private final RxHttpClient rxHttpClient = RxHttpClient.create(asyncHttpClient);

    public ExportService(final ServicesProperties servicesProperties) {
        this.servicesProperties = servicesProperties;
    }

    @PostConstruct
    public void init() {
        Optional.ofNullable(servicesProperties.getExportService())
                .map(ServicesProperties.ServiceConfig::getThreadCount)
                .ifPresent(threadCount -> {
                    log.info("Set thread count for export service to {}", threadCount);
                    config.setIoThreadsCount(threadCount);
                });
    }

    public Maybe<String> export(Collection<MappedOffer> mappedOffers) {
        final BoundRequestBuilder boundRequestBuilder = asyncHttpClient
                .preparePost(createFakeResourceRequest())
                .setBody("{\"itemsCount\": " + mappedOffers.size() + "}");

        return rxHttpClient.prepare(boundRequestBuilder.build())
                .doOnSuccess(response -> log.info("exported {} offers", mappedOffers.size()))
                .map(Response::getResponseBody);
    }

    private String createFakeResourceRequest() {
        return servicesProperties.getExportService() != null && servicesProperties.getExportService().getExternalAPI()
                ? EXTERNAL_FAKE_POST_RESOURCE : INTERNAL_FAKE_POST_RESOURCE;
    }
}
