package de.idealo.demo.reactivedemo.execution;

import java.util.Collection;

import org.springframework.stereotype.Component;

import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PublishingChain {

    private final PublishingService publishingService;

    public Flowable<PublishingResult> publish(Collection<MappedItem> mappedItems) {
        return publishingService.publish(mappedItems)
                .toFlowable()
                .map(results -> new PublishingResult(mappedItems.size()));
    }
}
