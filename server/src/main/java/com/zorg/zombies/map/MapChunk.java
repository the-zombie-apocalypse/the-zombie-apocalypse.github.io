package com.zorg.zombies.map;

import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.model.Coordinates;
import reactor.core.CoreSubscriber;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapChunk {

    public static final int DEFAULT_WIDTH = 1024;
    public static final int DEFAULT_HEIGHT = 1024;

    private Map<String, CoreSubscriber<WorldChange>> userIdToSubscriber = new ConcurrentHashMap<>();

    public void addObject(String id, Coordinates coordinates, CoreSubscriber<WorldChange> subscriber) {
        userIdToSubscriber.put(id, subscriber);
    }

    public void notifyUsers(WorldChange change) {
        userIdToSubscriber.values().forEach(subscriber -> subscriber.onNext(change));
    }

    public void unregister(String id) {
        userIdToSubscriber.remove(id);
    }
}
