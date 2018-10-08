package com.zorg.zombies.model;

import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.service.UsersCommunicator;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.ReplayProcessor;

@Getter
@Setter
public class UserSubscriber extends UserData {

    protected final FluxProcessor<WorldChange, WorldChange> subscriber;
    protected final UsersCommunicator usersCommunicator;

    public UserSubscriber(String id, Coordinates coordinates, UsersCommunicator usersCommunicator) {
        super(id, coordinates);
        this.usersCommunicator = usersCommunicator;

        final ReplayProcessor<WorldChange> source = ReplayProcessor.create(256);
        final Flux<WorldChange> onTerminate = source.doOnTerminate(() -> usersCommunicator.unregister(id));

        subscriber = FluxProcessor.wrap(source, onTerminate);
    }
}
