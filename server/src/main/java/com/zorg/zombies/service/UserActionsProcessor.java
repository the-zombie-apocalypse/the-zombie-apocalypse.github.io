package com.zorg.zombies.service;

import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.ErrorCommand;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.model.User;
import com.zorg.zombies.service.exception.WrongMoveCommandException;

public class UserActionsProcessor extends FluxProcessorDelegatingSubscriber<Command, WorldChange> {

    private final User user;

    UserActionsProcessor(User user) {
        super(user.getSubscriber());
        this.user = user;
    }

    @Override
    public void onNext(Command command) {
//        System.out.println("onNext: " + command);

        if (command.isMoveChangeCommand()) {
            if (command.isMoveStartCommand()) user.act((UserMoveCommand) command);
            else if (command.isMoveStopCommand()) user.act((UserStopMoveCommand) command);
            else throw new WrongMoveCommandException(command);

        } else if (command.isErrorCommand()) {
            ErrorCommand errorCommand = (ErrorCommand) command;
            errorCommand.getError().printStackTrace(); // todo: log!
        }
    }

    @Override
    public void onComplete() {
        user.destroy();
    }

}
