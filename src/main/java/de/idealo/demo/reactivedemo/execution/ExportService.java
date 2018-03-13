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
public class ExportService {

    private final AsyncHttpClient asyncHttpClient = asyncHttpClient();
    private final RxHttpClient rxHttpClient = RxHttpClient.create(asyncHttpClient);

    public Maybe<String> export(Collection<MappedOffer> mappedOffers) {
        final BoundRequestBuilder boundRequestBuilder = asyncHttpClient
                .preparePost("http://localhost:8080/exports")
                .setBody("{\"itemsCount\": " + mappedOffers.size() + "}");
        return rxHttpClient.prepare(boundRequestBuilder.build())
                .map(Response::getResponseBody);
    }
}
