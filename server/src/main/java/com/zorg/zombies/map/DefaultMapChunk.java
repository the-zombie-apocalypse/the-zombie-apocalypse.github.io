package com.zorg.zombies.map;

import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.model.UserSubscriber;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultMapChunk implements MapChunk {

    public static final int DEFAULT_WIDTH = 1024;
    public static final int DEFAULT_HEIGHT = 1024;

    private Map<String, UserSubscriber> userIdToUser = new ConcurrentHashMap<>();

    @Override
    public void notifyUsers(WorldChange change) {
        userIdToUser.values().forEach(user -> user.getSubscriber().onNext(change));
    }

    @Override
    public void unregister(String id) {
        userIdToUser.remove(id);
    }

    @Override
    public void addObject(UserSubscriber userData) {
        userIdToUser.put(userData.getId(), userData);
    }

    @Override
    public Collection<UserSubscriber> getAllUsers() {
        return userIdToUser.values();
    }
}
