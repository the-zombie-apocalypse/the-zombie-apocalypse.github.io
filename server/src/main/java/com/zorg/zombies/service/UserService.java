package com.zorg.zombies.service;

import com.zorg.zombies.model.User;
import com.zorg.zombies.model.UserData;
import com.zorg.zombies.persistance.UserDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    private final UserIdDefiner userIdDefiner;
    private final UserDataRepository userDataRepository;
    private final UsersCommunicator usersCommunicator;

    @Autowired
    public UserService(UserIdDefiner userIdDefiner,
                       UserDataRepository userDataRepository,
                       UsersCommunicator usersCommunicator) {

        this.userIdDefiner = userIdDefiner;
        this.userDataRepository = userDataRepository;
        this.usersCommunicator = usersCommunicator;
    }

    public User createUser(String sessionId) {
        final String id = userIdDefiner.getUserId(sessionId);
        final UserData user = new UserData(id);
        return new User(userDataRepository.save(user), usersCommunicator);
    }
}
