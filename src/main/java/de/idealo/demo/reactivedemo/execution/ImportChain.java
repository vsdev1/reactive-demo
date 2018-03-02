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

    private static final int PUBLISHING_BATCH_SIZE = 30;

    private final ItemParser itemParser;
    private final MappingChain mappingChain;
    private final PublishingChain publishingChain;

    public Flowable<ImportResult> process() {
        LongAdder itemCount = new LongAdder();

        return itemParser.parseItems()
                .doOnNext(item -> itemCount.increment())
                .flatMap(mappingChain::mapAndFilter)
                .doOnNext(mappingResult -> log.info("mapping result: {} ", mappingResult))
                .buffer(PUBLISHING_BATCH_SIZE)
                .flatMap(publishingChain::publishAndStore)
                .map(publishingResult -> new ImportResult())
                .doOnComplete(() -> log.info("processed {} items", itemCount.longValue()))
                .doAfterTerminate(() -> log.info("after terminate"));
    }
}
