package com.zorg.zombies.service;

import com.zorg.zombies.model.User;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserService {

    private final static Map<String, User> USERS = new ConcurrentHashMap<>();

    private final UserIdDefiner userIdDefiner;

    public UserService(UserIdDefiner userIdDefiner) {
        this.userIdDefiner = userIdDefiner;
    }

    public User createUser(String sessionId) {
        final String id = userIdDefiner.getUserId(sessionId);
        final User user = new User(id);

        USERS.put(id, user);

        return user;
    }

    public User getUser(String userId) {
        return USERS.get(userId);
    }
}
