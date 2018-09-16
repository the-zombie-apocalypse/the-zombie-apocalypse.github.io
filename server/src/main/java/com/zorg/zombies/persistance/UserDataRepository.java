package com.zorg.zombies.persistance;

import com.zorg.zombies.model.UserData;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserDataRepository {

    private final static Map<String, UserData> USERS = new ConcurrentHashMap<>(); // temporary solution

    public UserData getUser(String userId) {
        return USERS.get(userId);
    }

    public UserData save(UserData user) {
        USERS.put(user.getId(), user);
        return user;
    }
}
