package de.idealo.demo.reactivedemo.iternal.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ExportController {

    @PostMapping(value = "/exports")
    @ResponseStatus(HttpStatus.CREATED)
    public void export(ExportRequest request) {
        log.info("exported: {}", request);
    }
}
