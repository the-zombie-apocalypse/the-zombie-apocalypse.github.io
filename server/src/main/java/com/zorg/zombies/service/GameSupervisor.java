package com.zorg.zombies.service;

import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxProcessor;

@Component
public class GameSupervisor {

    private final UserService userService;

    @Autowired
    public GameSupervisor(UserService userService) {
        this.userService = userService;
    }

    public FluxProcessor<Command, WorldChange> createGameActionsProcessor(String sessionId) {
        final User user = userService.createUser(sessionId);
        final GameActionsProcessor processor = new GameActionsProcessor(user);

        processor.doOnComplete(user::onDestroy).subscribe();

        return processor;
    }

}
