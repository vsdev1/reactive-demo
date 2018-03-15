package de.idealo.demo.reactivedemo.execution;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

import java.util.Collection;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Response;
import org.asynchttpclient.extras.rxjava2.RxHttpClient;
import org.springframework.stereotype.Service;

import io.reactivex.Maybe;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExportService {

    private static final int THREAD_COUNT = 1;

    private static final String THREAD_POOL_NAME = "post-export";
    private static final String EXTERNAL_FAKE_POST_RESOURCE = "https://jsonplaceholder.typicode.com/posts/";
    private static final String INTERNAL_FAKE_POST_RESOURCE = "http://localhost:8080/exports";

    private final DefaultAsyncHttpClientConfig.Builder config = config().setIoThreadsCount(THREAD_COUNT).setThreadPoolName(THREAD_POOL_NAME);
    private final AsyncHttpClient asyncHttpClient = asyncHttpClient(config);
    private final RxHttpClient rxHttpClient = RxHttpClient.create(asyncHttpClient);

    public Maybe<String> export(Collection<MappedOffer> mappedOffers) {
        final BoundRequestBuilder boundRequestBuilder = asyncHttpClient
                .preparePost(createFakeResourceRequest(true))
                .setBody("{\"itemsCount\": " + mappedOffers.size() + "}");

        return rxHttpClient.prepare(boundRequestBuilder.build())
                .doOnSuccess(response -> log.info("exported {} offers", mappedOffers.size()))
                .map(Response::getResponseBody);
    }

    private String createFakeResourceRequest(boolean external) {
        return external ? EXTERNAL_FAKE_POST_RESOURCE : INTERNAL_FAKE_POST_RESOURCE;
    }
}
