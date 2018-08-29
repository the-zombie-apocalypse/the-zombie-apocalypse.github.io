package com.zorg.zombies.service;

import com.zorg.zombies.model.User;
import com.zorg.zombies.service.ChangesNotifier;
import com.zorg.zombies.service.GameActionsProcessor;
import com.zorg.zombies.service.UserService;
import com.zorg.zombies.service.UserUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameActionsProcessorFactory {

    private final UserService userService;
    private final UserUpdater userUpdater;
    private final ChangesNotifier changesNotifier;

    @Autowired
    public GameActionsProcessorFactory(UserService userService,
                          UserUpdater userUpdater,
                          ChangesNotifier changesNotifier) {

        this.userService = userService;
        this.userUpdater = userUpdater;
        this.changesNotifier = changesNotifier;
    }

    public GameActionsProcessor createFor(User user) {
        return new GameActionsProcessor(userService, userUpdater, changesNotifier, user);
    }

}
