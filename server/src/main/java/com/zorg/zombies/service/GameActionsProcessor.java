package com.zorg.zombies.service;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.ErrorCommand;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.model.User;
import com.zorg.zombies.service.exception.WrongMoveCommandException;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.ReplayProcessor;

public class GameActionsProcessor extends FluxProcessor<Command, WorldChange> {

    private final User user;
    private final ReplayProcessor<WorldChange> subscriber;

    GameActionsProcessor(User user) {
        this.user = user;
        subscriber = user.getSubscriber();
    }

    @Override
    public void onSubscribe(Subscription s) {
        System.out.println("onSubscribe: " + s);
    }

    @Override
    public void onNext(Command command) {
        System.out.println("onNext: " + command);

        if (command.isMoveChangeCommand()) {
            final UserChange userChange;

            if (command.isMoveStartCommand()) userChange = user.act((UserMoveCommand) command);
            else if (command.isMoveStopCommand()) userChange = user.act((UserStopMoveCommand) command);
            else throw new WrongMoveCommandException(command);

            if (userChange.isUpdated()) {
                // todo: notify other users!
            }
        } else if (command.isErrorCommand()) {
            ErrorCommand errorCommand = (ErrorCommand) command;
            errorCommand.getError().printStackTrace(); // todo: log!
        }
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("onError: " + t);
    }

    @Override
    public void onComplete() {
        System.out.println("onComplete");
        subscriber.onComplete();
    }

    @Override
    public void subscribe(CoreSubscriber<? super WorldChange> actual) {
        System.out.println("subscribe: " + actual);
        subscriber.subscribe(actual);
    }

    public ReplayProcessor<WorldChange> getSubscriber() {
        return subscriber;
    }
}
