package de.idealo.demo.reactivedemo.execution;

import java.util.Collection;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExportChain {

    private final ExportService exportService;

    public Mono<ExportResult> export(Collection<MappedOffer> mappedOffers) {
        return exportService.export(mappedOffers)
                .map(results -> new ExportResult(mappedOffers.size()));
    }
}
