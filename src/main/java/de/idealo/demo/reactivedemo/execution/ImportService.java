package de.idealo.demo.reactivedemo.execution;

import org.springframework.stereotype.Service;

import io.reactivex.schedulers.Schedulers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImportService {

    private final ImportChain importChain;

    public void startJob() {
        importChain.process()
                .subscribeOn(Schedulers.single())
                .doOnCancel(() -> log.warn("cancelled"))
                .doOnTerminate(() -> log.info("terminated"))
                .subscribe(exportResult -> log.info("result: {}", exportResult),
                        err -> log.error("error", err));
    }
}