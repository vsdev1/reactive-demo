package de.idealo.demo.reactivedemo.execution;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import de.idealo.demo.reactivedemo.data.Items;

@Component
@Slf4j
@RequiredArgsConstructor
public class MappingChain {

    private static final long KEEP_ALIVE_MS = 1000L;
    private static final int MAX_WAIT_TERMINATION_SECS = 10;
    private static final int QUEUE_SIZE_FACTOR = 10;
    private static final int THREAD_COUNT = 40;

    private ExecutorService mappingExecutor;
    private final MetaDataService metaDataService;

    public Flowable<MappingResult> mapAndFilter(Items items) {
        return Flowable.just(items)
                .subscribeOn(Schedulers.from(mappingExecutor))
                .flatMap(itemsToMap -> mapAndFilterInternal(items));
    }

    @PostConstruct
    public void init() {
        mappingExecutor = new ThreadPoolExecutor(
                THREAD_COUNT,
                THREAD_COUNT,
                KEEP_ALIVE_MS, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(THREAD_COUNT * QUEUE_SIZE_FACTOR, true),
                new CustomizableThreadFactory(getClass().getSimpleName() + "-"));
    }

    @PreDestroy
    public void shutdown() {
        mappingExecutor.shutdown();
        try {
            mappingExecutor.awaitTermination(MAX_WAIT_TERMINATION_SECS, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private Flowable<MappingResult> mapAndFilterInternal(Items items) {
        // TODO: might be removed later, is just to try async http client
        metaDataService.getMetaData(items.getProductTitle())
                .subscribe(s -> log.info("got meta data: {}", s));
        return Flowable.just(new MappingResult());
    }
}
