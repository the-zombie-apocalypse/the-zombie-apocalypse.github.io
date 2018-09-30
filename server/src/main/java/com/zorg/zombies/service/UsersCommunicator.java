package com.zorg.zombies.service;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.map.MapChunk;
import com.zorg.zombies.map.MapChunkSupervisor;
import com.zorg.zombies.model.Coordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.CoreSubscriber;

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
// todo: implement reaction on user's step
//        }

        chunk.notifyUsers(change);
    }

    public void register(String id, Coordinates coordinates, CoreSubscriber<WorldChange> subscriber) {
        final MapChunk chunk = mapChunkSupervisor.getChunkFor(coordinates);
        chunk.addObject(id, coordinates, subscriber);
    }

    public void unregister(String id) {
        mapChunkSupervisor.getChunkFor(id).unregister(id);
    }
}
