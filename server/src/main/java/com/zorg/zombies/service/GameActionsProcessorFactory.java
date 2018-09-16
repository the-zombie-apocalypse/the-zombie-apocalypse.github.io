package com.zorg.zombies.service;

import com.zorg.zombies.model.User;
import org.springframework.stereotype.Component;

@Component
public class GameActionsProcessorFactory {

    public GameActionsProcessor createFor(User user) {
        final GameActionsProcessor processor = new GameActionsProcessor(user);
        user.setProcessor(processor);
        return processor;
    }

}
