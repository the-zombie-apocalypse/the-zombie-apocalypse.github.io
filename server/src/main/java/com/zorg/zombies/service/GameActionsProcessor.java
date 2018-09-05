package com.zorg.zombies.service;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.change.WorldOnLoad;
import com.zorg.zombies.command.Command;
import com.zorg.zombies.command.MoveDirectionCommand;
import com.zorg.zombies.model.MoveDirection;
import com.zorg.zombies.model.User;
import com.zorg.zombies.service.exception.WrongMoveCommandException;
import lombok.val;
import org.reactivestreams.Subscription;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.ReplayProcessor;

import java.util.function.BiFunction;

public class GameActionsProcessor extends FluxProcessor<Command, WorldChange> {

    private final UserService userService;
    private final UserUpdater userUpdater;
    private final ChangesNotifier changesNotifier;

    private final ReplayProcessor<WorldChange> subscriber = ReplayProcessor.create(256);

    GameActionsProcessor(UserService userService, UserUpdater userUpdater, ChangesNotifier changesNotifier, User user) {
        this.userService = userService;
        this.userUpdater = userUpdater;
        this.changesNotifier = changesNotifier;
        this.subscriber.onNext(new WorldOnLoad(new UserChange(user.getId())));
    }

    @Override
    public void onSubscribe(Subscription s) {
        System.out.println("onSubscribe: " + s);
    }

    @Override
    public void onNext(Command command) {
        System.out.println("onNext: " + command);

        if (command.isMoveChangeCommand()) {
            val direction = ((MoveDirectionCommand) command).getDirection();

            final BiFunction<User, MoveDirection, UserChange> updateAction;

            if (command.isMoveStartCommand()) updateAction = userUpdater::updateUserMove;
            else if (command.isMoveStopCommand()) updateAction = userUpdater::updateUserStopMove;
            else throw new WrongMoveCommandException(command);

            val user = userService.getUser(command.getUserId());
            val userChange = updateAction.apply(user, direction);

            if (userChange.isUpdated()) {
                changesNotifier.notifyUserUpdate(userChange);
                // todo: update other users!
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
