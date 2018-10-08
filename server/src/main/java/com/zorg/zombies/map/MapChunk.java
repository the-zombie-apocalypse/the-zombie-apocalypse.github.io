package com.zorg.zombies.map;

import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.model.UserSubscriber;

import java.util.Collection;

public interface MapChunk {
    void notifyUsers(WorldChange change);

    void unregister(String id);

    void addObject(UserSubscriber userData);

    Collection<UserSubscriber> getAllUsers();
}
