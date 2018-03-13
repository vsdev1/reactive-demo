package de.idealo.demo.reactivedemo.execution;

import java.util.Collection;

import org.springframework.stereotype.Component;

import io.reactivex.Flowable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExportChain {

    private final ExportService exportService;

    public Flowable<ExportResult> export(Collection<MappedOffer> mappedOffers) {
        return exportService.export(mappedOffers)
                .toFlowable()
                .map(results -> new ExportResult(mappedOffers.size()));
    }
}
