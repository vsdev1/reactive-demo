package de.idealo.demo.reactivedemo.execution;

import java.util.List;

import org.springframework.stereotype.Component;

import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class PublishingChain {

    public Flowable<PublishingResult> publishAndStore(List<MappingResult> mappingResults) {
        return Flowable.just(mappingResults)
                .map(results -> new PublishingResult());
    }
}
