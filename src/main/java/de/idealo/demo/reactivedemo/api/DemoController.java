package de.idealo.demo.reactivedemo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class DemoController {

    @GetMapping("/tweets")
    public Flux<Item> getAllItems() {
        return Flux.just(new Item());
    }
}
