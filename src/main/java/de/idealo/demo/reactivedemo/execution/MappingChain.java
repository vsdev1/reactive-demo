package de.idealo.demo.reactivedemo.execution;

import org.springframework.stereotype.Component;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import de.idealo.demo.reactivedemo.data.Product;

@Component
@Slf4j
@RequiredArgsConstructor
public class MappingChain {

    private final MetaDataService metaDataService;

    public Flowable<MappedOffer> map(Product product) {
        final String productTitle = product.getProductTitle();

        return metaDataService.getMetaData(productTitle)
                .toFlowable()
                .observeOn(Schedulers.computation())
                .zipWith(product.getOffers(),
                        (metaData, offer) -> {
                            log.info("create mapped offer with meta data");
                            return new MappedOffer(productTitle, offer.getMerchantName(), offer.getPrice(), metaData);
                        });
    }
}
