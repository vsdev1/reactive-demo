package de.idealo.demo.reactivedemo.iternal.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class MetaDataController {

    @GetMapping("/meta-data")
    public Flux<String> getMetaData(@RequestParam("productTitle") String productTitle) {
        try {
            Thread.sleep((long) (Math.random() * 10));
        } catch (InterruptedException e) {
            throw new RuntimeException("delayed thread interrupted", e);
        }

        return Flux.just("meta data for " + productTitle + " at " + System.currentTimeMillis());
    }

}
