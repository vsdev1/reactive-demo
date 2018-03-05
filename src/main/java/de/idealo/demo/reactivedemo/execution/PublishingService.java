package de.idealo.demo.reactivedemo.execution;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import java.util.Collection;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.extras.rxjava2.RxHttpClient;
import org.springframework.stereotype.Service;

import io.reactivex.Maybe;

@Service
public class PublishingService {

    private final AsyncHttpClient asyncHttpClient = asyncHttpClient();
    private final RxHttpClient rxHttpClient = RxHttpClient.create(asyncHttpClient);

    public Maybe<String> publish(Collection<MappedItem> mappedItems) {
        final BoundRequestBuilder boundRequestBuilder = asyncHttpClient
                .preparePost("http://localhost:8080/exports")
                .setBody("{\"itemsCount\": " + mappedItems.size() + "}");
        return rxHttpClient.prepare(boundRequestBuilder.build())
                .map(Response::getResponseBody);
    }
}
