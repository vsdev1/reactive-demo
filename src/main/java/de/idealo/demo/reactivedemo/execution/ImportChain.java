package de.idealo.demo.reactivedemo.execution;

import java.util.concurrent.atomic.LongAdder;

import org.springframework.stereotype.Component;

import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import de.idealo.demo.reactivedemo.data.ItemParser;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImportChain {

    private static final int PUBLISHING_BATCH_SIZE = 2;

    private final ItemParser itemParser;
    private final MappingChain mappingChain;
    private final PublishingChain publishingChain;

    public Flowable<PublishingResult> process() {
        LongAdder itemCount = new LongAdder();

        return itemParser.parseItems()
                .take(5) // TODO: just for testing (to have less data)
                .doOnNext(item -> itemCount.increment())
                .flatMap(mappingChain::map)
                .doOnNext(mappingResult -> log.info("mapping result: {} ", mappingResult))
                .buffer(PUBLISHING_BATCH_SIZE)
                .flatMap(publishingChain::publish)
                .doOnComplete(() -> log.info("processed {} items", itemCount.longValue()))
                .doOnError(e -> log.error("an error occurred", e));
    }
}
