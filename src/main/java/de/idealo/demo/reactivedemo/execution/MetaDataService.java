package de.idealo.demo.reactivedemo.execution;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

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
public class MetaDataService {

    private static final int THREAD_COUNT = 1;

    private static final String THREAD_POOL_NAME = "get-metaData";
    private static final String EXTERNAL_FAKE_GET_RESOURCE = "https://jsonplaceholder.typicode.com/posts/1";
    private static final String INTERNAL_FAKE_GET_RESOURCE = "http://localhost:8080/meta-data?productTitle=";

    private final DefaultAsyncHttpClientConfig.Builder config = config().setIoThreadsCount(THREAD_COUNT).setThreadPoolName(THREAD_POOL_NAME);
    private final AsyncHttpClient asyncHttpClient = asyncHttpClient(config);
    private final RxHttpClient rxHttpClient = RxHttpClient.create(asyncHttpClient);

    public Maybe<String> getMetaData(String productTitle) {
        final BoundRequestBuilder boundRequestBuilder = asyncHttpClient
                .prepareGet(createFakeResourceRequest(productTitle, true));

        return rxHttpClient.prepare(boundRequestBuilder.build())
                .doOnSuccess(response -> log.info("got meta data for product '{}'", productTitle))
                .map(Response::getResponseBody);
    }

    private String createFakeResourceRequest(String productTitle, boolean external) {
        return external ? EXTERNAL_FAKE_GET_RESOURCE : INTERNAL_FAKE_GET_RESOURCE + productTitle;
    }
}
