package com.zorg.zombies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zorg.zombies.change.WorldChange;
import lombok.Getter;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.ReplayProcessor;

public class UserSubscriber extends UserData {

    @Getter
    @JsonIgnore
    private final FluxProcessor<WorldChange, WorldChange> subscriber;

    public UserSubscriber(UserSubscriber from) {
        super(from);
        this.subscriber = from.getSubscriber();
    }

    public UserSubscriber(String id, Coordinates coordinates) {
        super(id, coordinates);
        subscriber = ReplayProcessor.create(256);
    }
}
