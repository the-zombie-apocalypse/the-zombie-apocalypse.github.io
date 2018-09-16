package com.zorg.zombies.service;

import com.zorg.zombies.model.User;
import com.zorg.zombies.model.UserData;
import com.zorg.zombies.persistance.UserDataRepository;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private final UserIdDefiner userIdDefiner;
    private final UserDataRepository userDataRepository;

    public UserService(UserIdDefiner userIdDefiner, UserDataRepository userDataRepository) {
        this.userIdDefiner = userIdDefiner;
        this.userDataRepository = userDataRepository;
    }

    public User createUser(String sessionId) {
        final String id = userIdDefiner.getUserId(sessionId);
        final UserData user = new UserData(id);
        return new User(userDataRepository.save(user));
    }
}
