package de.idealo.demo.reactivedemo.iternal.api;

import java.time.Duration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class MetaDataController {

    private static final long DELAY_IN_MILLIS = 1000L;

    @GetMapping("/meta-data")
    public Mono<String> getMetaData(@RequestParam("productTitle") String productTitle) {
        return Mono.just("meta data for " + productTitle + " at " + System.currentTimeMillis())
                .delaySubscription(Duration.ofMillis(DELAY_IN_MILLIS));
    }
}
