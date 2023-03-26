package projectreator_essentials;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

@Slf4j
class FluxTest {

    @Test
    void fluxSubscriberTest() {
        Flux<String> fluxString = Flux.just("Fulano", "sicrano").log();

        StepVerifier.create(fluxString)
                .expectNext("Fulano", "sicrano")
                .verifyComplete();
    }

    @Test
    void fluxSubscriberNumbersTest() {
        Flux<Integer> fluxIntegers = Flux.range(1, 3).log();
        fluxIntegers.subscribe(n -> log.info("{}", n));

        StepVerifier.create(fluxIntegers)
                .expectNext(1,2,3)
                .verifyComplete();
    }

    @Test
    void fluxSubscriberFromListTest() {
        Flux<Integer> fluxIntegers = Flux.fromIterable(List.of(1,2,3)).log()
                .map(n -> {
                    if(n == 2) throw new RuntimeException("deu ruim");
                    return n;
                });

        fluxIntegers.subscribe(n -> log.info("{}", n));

        StepVerifier.create(fluxIntegers)
                .expectNext(1)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void fluxSubscriberIntervalTest() throws InterruptedException {
        // the task on a background thread
        Flux<Long> interval = Flux.interval(Duration.ofMillis(100)).log();

        interval.subscribe(aLong -> log.info("{}", aLong));

        Thread.sleep(3000);
    }

    @Test
    void fluxSubscriberIntervalWithTakeTest() throws InterruptedException {
        // the task run on a background thread
        // interval run a different thread
        Flux<Long> interval = getInterval();

        interval.subscribe(aLong -> log.info("{}", aLong));

        Thread.sleep(3000);
    }

    @Test
    void fluxSubscriberIntervalTestCheck() throws InterruptedException {
        StepVerifier.withVirtualTime(this::getInterval)
                .expectSubscription()
                .expectNoEvent(Duration.ofDays(1))
                .thenAwait(Duration.ofDays(1))
                .expectNext(0L)
                .thenAwait(Duration.ofDays(1))
                .expectNext(1L)
                .thenCancel()
                .verify();

    }

    private Flux<Long> getInterval() {
        return Flux.interval(Duration.ofDays(1)).log();
    }

    @Test
    void connectableFlux() throws InterruptedException {
        ConnectableFlux<Integer> connectableFlux = Flux.range(1, 10)
                .delayElements(Duration.ofMillis(100))
                .publish();

        //connectableFlux.connect();

        //Thread.sleep(300);

        //connectableFlux.subscribe(integer -> log.info("sub1 {}", integer));

        //Thread.sleep(300);

        //connectableFlux.subscribe(integer -> log.info("sub2 {}", integer));

        StepVerifier
                .create(connectableFlux)
                .then(connectableFlux::connect)
                .expectNext(1,2,3,4,5,6,7,8,9,10)
                .expectComplete()
                .verify();
    }
}
