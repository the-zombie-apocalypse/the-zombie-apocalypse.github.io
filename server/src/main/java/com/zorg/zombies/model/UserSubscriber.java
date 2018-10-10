package com.zorg.zombies.model;

import com.zorg.zombies.change.WorldChange;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.ReplayProcessor;

@Getter
@Setter
public class UserSubscriber extends UserData {

    protected final FluxProcessor<WorldChange, WorldChange> subscriber = ReplayProcessor.create(256);

    public UserSubscriber(String id, Coordinates coordinates) {
        super(id, coordinates);
    }
}
