package io.eddumelendez.reactorkotlin

import io.eddumelendez.reactorkotlin.domain.User
import org.junit.Assert.fail
import org.junit.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.lang.RuntimeException
import java.time.Duration
import java.util.function.Supplier

class Part03StepVerifier {

    @Test
    fun expectElementsThenComplete() {
        expectFooBarComplete(Flux.just("foo", "bar"))
    }

    // TODO Use StepVerifier to check that the flux parameter emits "foo" and "bar" elements then completes successfully.
    fun expectFooBarComplete(flux: Flux<String>) {
        StepVerifier.create(flux)
                .expectNext("foo")
                .expectNext("bar")
                .expectComplete()
    }

    @Test
    fun expect2ElementsThenError() {
        expectFooBarError(Flux.just("foo", "bar").concatWith(Mono.error(RuntimeException())))
    }

    // TODO Use StepVerifier to check that the flux parameter emits "foo" and "bar" elements then a RuntimeException error.
    fun expectFooBarError(flux: Flux<String>) {
        StepVerifier.create(flux)
                .expectNext("foo")
                .expectNext("bar")
                .expectError(RuntimeException::class.java)
    }

    @Test
    fun expectElementsWithThenComplete() {
        expectSkylerJesseComplete(Flux.just(User("swhite", null, null), User("jpinkman", null, null)))
    }

    // TODO Use StepVerifier to check that the flux parameter emits a User with "swhite" username and another one with "jpinkman" then completes successfully.
    fun expectSkylerJesseComplete(flux: Flux<User>) {
        StepVerifier.create(flux)
                .expectNextMatches { u -> u.username.equals("swhite") }
                .expectNextMatches { u -> u.username.equals("jpinkman") }
                .expectComplete()
    }

    @Test
    fun count() {
        expect10Elements(Flux.interval(Duration.ofSeconds(1)).take(10));
    }

    // TODO Expect 10 elements then complete and notice how long it takes for running the test
    fun expect10Elements(flux: Flux<Long>) {
        StepVerifier.create(flux, 10)
                .expectComplete()
    }

    @Test
    fun countWithVirtualTime() {
        expect3600Elements(Supplier<Flux<Long>> { Flux.interval(Duration.ofSeconds(1)).take(3600) })
    }

    // TODO Expect 3600 elements then complete using the virtual time capabilities provided via StepVerifier.withVirtualTime() and notice how long it takes for running the test
    fun expect3600Elements(supplier: Supplier<Flux<Long>>) {
        StepVerifier.withVirtualTime(supplier, 3600)
                .thenAwait(Duration.ofHours(1))
                .expectComplete()

    }

}
