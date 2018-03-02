package de.idealo.demo.reactivedemo.execution;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.extras.rxjava2.RxHttpClient;
import org.springframework.stereotype.Component;

import io.reactivex.Maybe;

@Component
public class MetaDataService {

    private final AsyncHttpClient asyncHttpClient = asyncHttpClient();
    private final RxHttpClient rxHttpClient = RxHttpClient.create(asyncHttpClient);

    public Maybe<String> getMetaData(String productTitle) {
        final BoundRequestBuilder boundRequestBuilder = asyncHttpClient
                .prepareGet("http://localhost:8080/meta-data?productTitle=" + productTitle);
        return rxHttpClient.prepare(boundRequestBuilder.build())
                .map(response -> response.getResponseBody());
    }
}
