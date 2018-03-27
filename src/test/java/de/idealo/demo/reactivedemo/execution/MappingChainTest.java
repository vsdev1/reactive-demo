package de.idealo.demo.reactivedemo.execution;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Duration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import de.idealo.demo.reactivedemo.data.Offer;
import de.idealo.demo.reactivedemo.data.Product;

@RunWith(MockitoJUnitRunner.class)
public class MappingChainTest {

    private static final String PRODUCT_TITLE = "product title";
    private static final String MOCK_META_DATA = "mock meta data";
    private static final String MERCHANT_NAME_1 = "merchant name 1";
    private static final BigDecimal PRICE_1 = new BigDecimal(10.99D);
    private static final String MERCHANT_NAME_2 = "merchant name 2";
    private static final BigDecimal PRICE_2 = new BigDecimal(123.23D);

    @Mock
    private MetaDataService metaDataServiceMock;

    @InjectMocks
    private MappingChain mappingChain;

    @Test
    public void map() {
        // given
        when(metaDataServiceMock.getMetaData(anyString()))
                .thenReturn(Mono.just(MOCK_META_DATA));

        final Product product = new Product(PRODUCT_TITLE);
        product.addOffer(new Offer(MERCHANT_NAME_1, PRICE_1));
        product.addOffer(new Offer(MERCHANT_NAME_2, PRICE_2));

        // when and then
        StepVerifier.withVirtualTime(() -> mappingChain.map(product))
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(2))
                .expectNextCount(2)
                .verifyComplete();

        verify(metaDataServiceMock).getMetaData(PRODUCT_TITLE);
    }
}