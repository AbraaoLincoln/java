# Reactive Streams

- Asynchronous
- Non-blocking
- Backpressure

## Interfaces

### Publisher

- emits events
- calls _onSubscribe(with subscription)_ on Subscriber
- calls _onNext_ on Subscriber, until:
  - sends all the objects requested
  - sends all objects it has, then call _onComplete_ and Subscription will be canceled
  - there is an error, then call _onError_ on nd Subscription will be canceled

### Subscriber

- subscribe on a Publisher
- react to an event
- can set backpressure through Subscription

### Subscription

- object use by Publisher and Subscriber, kind of context.