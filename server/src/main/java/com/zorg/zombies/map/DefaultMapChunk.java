package com.zorg.zombies.map;

import com.zorg.zombies.change.UserLeftGameEvent;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.model.User;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class DefaultMapChunk implements MapChunk {

    public static final int DEFAULT_WIDTH = 1024;
    public static final int DEFAULT_HEIGHT = 1024;

    private volatile Map<String, User> userIdToUser = new ConcurrentHashMap<>();

    @Override
    public void notifyUsers(WorldChange change) {
        for (User user : userIdToUser.values()) {
            try {
                user.getSubscriber().onNext(change);
            } catch (Exception e) {
                log.error("Session " + user.getId() + " ended.", e);
                user.destroy();

                var processor = user.getSubscriber();

                if (!processor.isTerminated()) {
                    processor.onComplete();
                }
            }
        }
    }

    @Override
    public void unregister(String id) {
        userIdToUser.remove(id);
        notifyUsers(new WorldChange<>(new UserLeftGameEvent(id)));
    }

    @Override
    public void addObject(User userData) {
        userIdToUser.put(userData.getId(), userData);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userIdToUser.values();
    }
}
