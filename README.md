# Demo of rxjava 2 and Spring reactor

[![Build Status](https://travis-ci.org/vsdev1/reactive-demo.svg?branch=master)](https://travis-ci.org/vsdev1/reactive-demo) 

This project demonstrates the usage of [rxjava 2](https://github.com/ReactiveX/RxJava) and [Spring reactor](https://projectreactor.io/). It 
1. reads data from a CSV file, 
2. converts those data,
3. enriches them with meta data from an external REST service (via an asynchronous non-blocking http client),
4. exports them to another REST service.

This workflow is triggered by the REST request `POST http://localhost:8080/imports`.

The external REST services can be either external or internal ones (configured in `application.properties`). The internal services has a delay and can therefore be useful to test how the async http client behaves with different thread pool sizes. 

###Further reading:
* [rxjava documentation](https://github.com/ReactiveX/RxJava/wiki)
* [Spring reactor documentation](http://projectreactor.io/docs/core/release/reference/)
* [Comparing Java 8, RxJava, Reactor](http://alexsderkach.io/comparing-java-8-rxjava-reactor/)
* [Reactive Streams in Java 9](https://dzone.com/articles/reactive-streams-in-java-9)
* [Interactive diagrams of Rx Observables](http://rxmarbles.com/)
* [Schedulers](http://www.baeldung.com/rxjava-schedulers)
* [Backpressure](http://www.baeldung.com/rxjava-backpressure)
* [Error handling](http://www.baeldung.com/rxjava-error-handling)
* [Async http client for rxjava 2](https://github.com/AsyncHttpClient/async-http-client/tree/master/extras/rxjava2)
* [Async http client for Spring reactor](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-client)
