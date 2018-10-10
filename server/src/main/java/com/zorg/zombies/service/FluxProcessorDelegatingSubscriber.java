package com.zorg.zombies.service;

import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.FluxProcessor;

public abstract class FluxProcessorDelegatingSubscriber<IN, OUT> extends FluxProcessor<IN, OUT> {

    protected final FluxProcessor<OUT, OUT> subscriber;

    public FluxProcessorDelegatingSubscriber(FluxProcessor<OUT, OUT> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onSubscribe(Subscription s) {
        subscriber.onSubscribe(s);
    }

    @Override
    public void onError(Throwable t) {
        subscriber.onError(t);
    }

    @Override
    public void onComplete() {
        subscriber.onComplete();
    }

    @Override
    public void subscribe(CoreSubscriber<? super OUT> actual) {
        subscriber.subscribe(actual);
    }
}
