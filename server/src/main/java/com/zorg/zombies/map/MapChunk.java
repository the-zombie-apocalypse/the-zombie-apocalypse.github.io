package com.zorg.zombies.map;

import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.model.Coordinates;
import reactor.core.CoreSubscriber;

public interface MapChunk {
    void addObject(String id, Coordinates coordinates, CoreSubscriber<WorldChange> subscriber);

    void notifyUsers(WorldChange change);

    void unregister(String id);
}
