package com.zorg.zombies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zorg.zombies.change.WorldChange;
import lombok.Getter;
import reactor.core.publisher.FluxProcessor;
import reactor.extra.processor.TopicProcessor;

public class UserSubscriber extends UserData {

    @Getter
    @JsonIgnore
    private final FluxProcessor<WorldChange, WorldChange> subscriber;

    public UserSubscriber(UserSubscriber from) {
        super(from);
        this.subscriber = from.getSubscriber();
    }

    public UserSubscriber(String id, Coordinates coordinates, String nickname) {
        super(id, coordinates, nickname);
        subscriber = TopicProcessor.share(id, 8);
    }

    public UserSubscriber(String id, Coordinates coordinates) {
        super(id, coordinates);
        subscriber = TopicProcessor.share(id, 8);
    }
}
