package com.zorg.zombies.service;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.change.WorldOnLoad;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.model.User;
import lombok.val;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.ReplayProcessor;

public class GameActionsProcessor extends FluxProcessor<Command, WorldChange> {

    private final UserService userService;
    private final UserUpdater userUpdater;
    private final ChangesNotifier changesNotifier;

    private final User user;
    private final ReplayProcessor<WorldChange> subscriber = ReplayProcessor.create(256);

    GameActionsProcessor(UserService userService, UserUpdater userUpdater, ChangesNotifier changesNotifier, User user) {
        this.userService = userService;
        this.userUpdater = userUpdater;
        this.changesNotifier = changesNotifier;
        this.user = user;
        this.subscriber.onNext(new WorldOnLoad(new UserChange(user.getId())));
    }

    @Override
    public void onSubscribe(Subscription s) {
        System.out.println("onSubscribe: " + s);
    }

    @Override
    public void onNext(Command command) {
        System.out.println("onNext: " + command);

        if (command.isMoveCommand()) {
            final UserMoveCommand moveCommand = (UserMoveCommand) command;

            val userMoveDirection = moveCommand.getMoveDirection();
            val userId = command.getUserId();
            val user = userService.getUser(userId);

            val userChange = userUpdater.updateUserMove(user, userMoveDirection);

            if (userChange.isUpdated()) {
                changesNotifier.notifyUserUpdate(userChange);
            }
        } else if (command.isStopMoveCommand()) {
            final UserStopMoveCommand stopMoveCommand = (UserStopMoveCommand) command;
            val userStopMoveDirection = stopMoveCommand.getStopMoveDirection();
            val userId = command.getUserId();
            val user = userService.getUser(userId);

            val userChange = userUpdater.updateUserStopMove(user, userStopMoveDirection);

            if (userChange.isUpdated()) {
                changesNotifier.notifyUserUpdate(userChange);
            }
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
