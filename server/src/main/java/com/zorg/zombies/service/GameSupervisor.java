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
    private final GameActionsProcessorFactory gameActionsProcessorFactory;

    @Autowired
    public GameSupervisor(UserService userService,
                          GameActionsProcessorFactory gameActionsProcessorFactory) {

        this.userService = userService;
        this.gameActionsProcessorFactory = gameActionsProcessorFactory;
    }

    public FluxProcessor<Command, WorldChange> createGameActionsProcessor(String sessionId) {
        final User user = userService.createUser(sessionId);
        final GameActionsProcessor processor = gameActionsProcessorFactory.createFor(user);

        processor.doOnComplete(user::onDestroy).subscribe();

        return processor;
    }

}
