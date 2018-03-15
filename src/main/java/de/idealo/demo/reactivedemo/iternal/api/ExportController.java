package de.idealo.demo.reactivedemo.iternal.api;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExportController {

    private static final long DELAY_IN_MILLIS = 1000L;

    @PostMapping(value = "/exports")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Integer> export(ExportRequest request) {
        log.info("exported");

        return Mono.just(request.getItemsCount())
                .delaySubscription(Duration.ofMillis(DELAY_IN_MILLIS));
    }
}
