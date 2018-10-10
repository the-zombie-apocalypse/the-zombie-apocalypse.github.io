package com.zorg.zombies.service;

import com.zorg.zombies.change.NewUserJoined;
import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserPositionChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.change.WorldOnLoad;
import com.zorg.zombies.map.MapChunk;
import com.zorg.zombies.map.MapChunkSupervisor;
import com.zorg.zombies.model.UserSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxProcessor;

import java.util.Collection;

@Service
public class UsersCommunicator {

    private final MapChunkSupervisor mapChunkSupervisor;

    @Autowired
    public UsersCommunicator(MapChunkSupervisor mapChunkSupervisor) {
        this.mapChunkSupervisor = mapChunkSupervisor;
    }

    public void notifyUsers(WorldChange change) {
        final UserChange userChange = change.getUser();

        final String userChangeId = userChange.getId();
        final MapChunk chunk = mapChunkSupervisor.getChunkFor(userChangeId);

//        if (userChange.isPositionChange()) {
//            final UserPositionChange userPositionChange = (UserPositionChange) userChange;
//            final Coordinates changeCoordinates = userPositionChange.getCoordinates();
//            // todo: implement reaction on user's step
//        }

        chunk.notifyUsers(change);
    }

    public void register(UserSubscriber user) {
        final MapChunk chunk = mapChunkSupervisor.getChunkFor(user.getCoordinates());

        final FluxProcessor<WorldChange, WorldChange> subscriber = user.getSubscriber();

        final UserPositionChange userPositionChange = new UserPositionChange(user);
        final Collection<UserSubscriber> allUsers = chunk.getAllUsers();

        subscriber.onNext(new WorldOnLoad(userPositionChange, allUsers));
        notifyUsers(new NewUserJoined(userPositionChange));

        chunk.addObject(user);
    }

    public void unregister(String id) {
        mapChunkSupervisor.getChunkFor(id).unregister(id);
    }
}
