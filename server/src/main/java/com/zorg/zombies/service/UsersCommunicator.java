package com.zorg.zombies.service;

import com.zorg.zombies.change.NewUserJoined;
import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.change.WorldOnLoad;
import com.zorg.zombies.map.MapChunk;
import com.zorg.zombies.map.MapChunkSupervisor;
import com.zorg.zombies.model.User;
import com.zorg.zombies.model.UserSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UsersCommunicator {

    private final MapChunkSupervisor mapChunkSupervisor;

    @Autowired
    public UsersCommunicator(MapChunkSupervisor mapChunkSupervisor) {
        this.mapChunkSupervisor = mapChunkSupervisor;
    }

    public void notifyUsers(WorldChange change) {
        UserChange userChange = change.getUser();

        String userChangeId = userChange.getId();
        MapChunk chunk = mapChunkSupervisor.getChunkFor(userChangeId);

//        if (userChange.isPositionChange()) {
//             UserPositionChange userPositionChange = (UserPositionChange) userChange;
//             Coordinates changeCoordinates = userPositionChange.getCoordinates();
//            // todo: implement reaction on user's step
//        }

        chunk.notifyUsers(change);
    }

    public void register(User user) {
        MapChunk chunk = mapChunkSupervisor.getChunkFor(user.getCoordinates());

        Collection<UserSubscriber> allUsers = chunk.getAllUsers()
                .stream()
                .map(UserSubscriber::new)
                .collect(Collectors.toSet());

        user.getSubscriber().onNext(new WorldOnLoad(user, allUsers));
        notifyUsers(new NewUserJoined(user));

        chunk.addObject(user);
    }

    public void unregister(String id) {
        mapChunkSupervisor.getChunkFor(id).unregister(id);
    }
}
