package com.zorg.zombies.map;

import com.zorg.zombies.change.UserLeftGameEvent;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultMapChunk implements MapChunk {

    private static final Logger logger = LoggerFactory.getLogger(DefaultMapChunk.class);
    public static final int DEFAULT_WIDTH = 1024;
    public static final int DEFAULT_HEIGHT = 1024;

    private volatile Map<String, User> userIdToUser = new ConcurrentHashMap<>();

    @Override
    public void notifyUsers(WorldChange change) {
        for (User user : userIdToUser.values()) {
            try {
                user.getSubscriber().onNext(change);
            } catch (Exception e) {
                logger.error("Looks like user " + user.getId() + " disconnected.", e);
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
