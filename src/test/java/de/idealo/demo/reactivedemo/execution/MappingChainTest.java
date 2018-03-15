package de.idealo.demo.reactivedemo.execution;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Maybe;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

import de.idealo.demo.reactivedemo.data.Offer;
import de.idealo.demo.reactivedemo.data.Product;

@RunWith(MockitoJUnitRunner.class)
public class MappingChainTest {

    private static final String PRODUCT_TITLE = "product title";
    private static final String MOCK_META_DATA = "mock meta data";
    private static final String MERCHANT_NAME_1 = "merchant name 1";
    private static final BigDecimal PRICE_1 = new BigDecimal(10.99D);

    @Captor
    private ArgumentCaptor<MappedOffer> mappedOfferArgumentCaptor;

    @Mock
    private MetaDataService metaDataServiceMock;

    @InjectMocks
    private MappingChain mappingChain;

    @Before
    public void setUp() {
        RxJavaPlugins.setComputationSchedulerHandler(scheduler -> Schedulers.trampoline());
    }

    @After
    public void tearDown() {
        RxJavaPlugins.reset();
    }

    @Test
    public void map() {
        // given
        when(metaDataServiceMock.getMetaData(anyString()))
                .thenReturn(Maybe.just(MOCK_META_DATA));

        final Product product = new Product(PRODUCT_TITLE);
        product.addOffer(new Offer(MERCHANT_NAME_1, PRICE_1));

        // when
        TestSubscriber<MappedOffer> subscriber = new TestSubscriber<>();
        mappingChain.map(product).subscribe(subscriber);

        // then
        subscriber.assertComplete();
        subscriber.assertValueCount(1);
        final MappedOffer mappedOffer = subscriber.values().get(0);
        assertThat(mappedOffer.getProductTitle(), is(PRODUCT_TITLE));
        assertThat(mappedOffer.getMerchantName(), is(MERCHANT_NAME_1));
        assertThat(mappedOffer.getPrice(), is(PRICE_1));
        assertThat(mappedOffer.getMetaData(), is(MOCK_META_DATA));

        verify(metaDataServiceMock).getMetaData(PRODUCT_TITLE);
    }
}