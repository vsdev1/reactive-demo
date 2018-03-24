package de.idealo.demo.reactivedemo.execution;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import de.idealo.demo.reactivedemo.data.Offer;
import de.idealo.demo.reactivedemo.data.Product;

@Component
@Slf4j
@RequiredArgsConstructor
public class MappingChain {

    private final MetaDataService metaDataService;

    public Flux<MappedOffer> map(Product product) {
        final String productTitle = product.getProductTitle();

        return Flux.just(true)
                .subscribeOn(Schedulers.elastic())
                .switchMap(dummy -> Flux.from(metaDataService.getMetaData(productTitle)))
                .retry(3)
                .onErrorReturn("fallback meta data")
                .publishOn(Schedulers.parallel())
                .zipWith(Flux.fromIterable(product.getOffers()))
                .map(objects -> {
                    log.info("create mapped offer with meta data");
                    final String metaData = objects.getT1();
                    final Offer offer = objects.getT2();

                    return new MappedOffer(productTitle, offer.getMerchantName(), offer.getPrice(), metaData);
                });
    }
}
