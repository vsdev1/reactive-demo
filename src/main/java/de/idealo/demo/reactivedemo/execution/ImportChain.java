package de.idealo.demo.reactivedemo.execution;

import java.util.concurrent.atomic.LongAdder;

import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import de.idealo.demo.reactivedemo.data.ProductParser;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImportChain {

    private static final int EXPORT_BATCH_SIZE = 2;

    private final ProductParser productParser;
    private final MappingChain mappingChain;
    private final ExportChain exportChain;

    public Flux<ExportResult> process() {
        LongAdder itemCount = new LongAdder();

        StopWatch stopWatch = new StopWatch("Offer import");
        stopWatch.start();
        return productParser.parseProduct()
                .take(7) // TODO: just for testing (to have less data)
                .doOnNext(item -> itemCount.increment())
                .flatMap(mappingChain::map)
                .buffer(EXPORT_BATCH_SIZE)
                .flatMap(exportChain::export)
                .doOnComplete(() -> {
                    log.info("processed {} offers", itemCount.longValue());
                    stopWatch.stop();
                    log.info("result: \n" + stopWatch.prettyPrint());
                })
                .doOnError(e -> log.error("an error occurred", e));
    }
}
