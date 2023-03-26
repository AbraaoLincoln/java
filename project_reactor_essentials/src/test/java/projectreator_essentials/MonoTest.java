package projectreator_essentials;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
class MonoTest {

    /*
     * Mono represent a stream of 1 or void
     * Mono in project reactor is a Publisher
     */

    @Test
    void monoSubscriberTest() {
        String name = "Fulano da Silva";
        Mono<String> mono = Mono.just(name);
        mono.subscribe();

        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    void monoSubscriberConsumerTest() {
        String name = "Fulano da Silva";
        Mono<String> mono = Mono.just(name);

        /*
            when calling subscribe we can pass a
            consumer(lambda) that will be executed receiving the value from the mono
         */
        mono.subscribe(System.out::println);

        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    void monoSubscriberConsumerErrorTest() {
        String name = "Fulano da Silva";
        Mono<String> mono = Mono.just(name).map(n -> {
            throw new RuntimeException("Test error on mono");
        });

        /*
            when calling subscribe we can pass a
            consumer(lambda) that will be executed receiving the value from the mono
            and a second consumer for error
         */
        mono.subscribe(System.out::println, e -> log.error("deu ruim", e));

        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void monoSubscriberCompleteTest() {
        String name = "Fulano da Silva";
        Mono<String> mono = Mono.just(name);

        /*
            when calling subscribe we can pass a
            1. consumer(lambda) that will be executed receiving the value from the mono
            2. consumer for error
            3. consumer for when the subscription is complete
         */
        mono.subscribe(System.out::println, Throwable::printStackTrace,
                () -> log.info("completed"));

        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    void monoSubscriberSubscriptionTest() {
        String name = "Fulano da Silva";
        Mono<String> mono = Mono.just(name);

        /*
            when calling subscribe we can pass a
            1. consumer(lambda) that will be executed receiving the value from the mono
            2. consumer for error
            3. consumer for when the subscription is complete
            4. consumer for doing stuff related with the subscription like configure backpressure
         */
        mono.subscribe(System.out::println, Throwable::printStackTrace,
                () -> log.info("completed"), subscription -> subscription.request(1));

        StepVerifier.create(mono)
                .expectNext(name)
                .verifyComplete();
    }

    @Test
    void monoDoOnMethodTest() {
        String name = "Fulano da Silva";

        /*
            the Publisher has several doOnSomething
            to add behavior
         */
        Mono<String> mono = Mono.just(name)
                .log()
                .doOnSubscribe(subscription -> log.info("doOnSubscribe"))
                .doOnRequest(value -> log.info("doOnRequest with value {}", value))
                .doOnNext(s -> log.info("doOnNext with value {}", s))
                .doOnSuccess(s -> log.info("doOnSuccess"));

        mono.subscribe();
    }

    @Test
    void monoDoOnErrorTest() {
        /*
            the doOnError is handler by the Publisher, its clone so there is no onNext
         */
        Mono<Object> error = Mono.error(new RuntimeException("deu ruim"))
                .doOnError(e -> log.error("doOnError {}", e.getMessage()))
                .log();

        StepVerifier.create(error)
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void monoDoOnErrorResumeTest() {
        /*
            the doOnError is handler by the Publisher, its clone so there is no onNext
         */
        Mono<Object> error = Mono.error(new RuntimeException("deu ruim"))
                .onErrorResume(throwable -> {
                    log.error("onErrorResume");
                    return Mono.just("test");
                })
                .log();

        StepVerifier.create(error)
                .expectNext("test")
                .verifyComplete();
    }

    @Test
    void monoDoOnErrorReturnTest() {
        /*
            the doOnError is handler by the Publisher, its clone so there is no onNext
         */
        Mono<Object> error = Mono.error(new RuntimeException("deu ruim"))
                .onErrorReturn("empty")
                .log();

        StepVerifier.create(error)
                .expectNext("empty")
                .verifyComplete();
    }
}
