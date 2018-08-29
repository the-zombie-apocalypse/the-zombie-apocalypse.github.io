package com.zorg.zombies.service;

import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.model.User;
import com.zorg.zombies.service.ChangesNotifier;
import com.zorg.zombies.service.GameActionsProcessor;
import com.zorg.zombies.service.GameActionsProcessorFactory;
import com.zorg.zombies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxProcessor;

@Component
public class GameSupervisor {

    private final UserService userService;
    private final GameActionsProcessorFactory gameActionsProcessorFactory;
    private final ChangesNotifier changesNotifier;

    @Autowired
    public GameSupervisor(UserService userService,
                          GameActionsProcessorFactory gameActionsProcessorFactory,
                          ChangesNotifier changesNotifier) {

        this.userService = userService;
        this.gameActionsProcessorFactory = gameActionsProcessorFactory;
        this.changesNotifier = changesNotifier;
    }

    public FluxProcessor<Command, WorldChange> createGameActionsProcessor(String sessionId) {
        final User user = userService.createUser(sessionId);
        final String userId = user.getId();

        final GameActionsProcessor processor = gameActionsProcessorFactory.createFor(user);
        changesNotifier.register(userId, processor);

        processor.doOnComplete(() -> changesNotifier.remove(userId)).subscribe();

        return processor;
    }

}
