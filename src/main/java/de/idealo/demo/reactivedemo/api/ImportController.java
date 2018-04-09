package de.idealo.demo.reactivedemo.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import de.idealo.demo.reactivedemo.execution.ImportService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;

    @PostMapping("/imports")
    @ResponseStatus(HttpStatus.CREATED)
    public void startImport() {
        importService.startJob();
    }
}
