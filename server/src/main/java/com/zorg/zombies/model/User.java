package com.zorg.zombies.model;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserPositionChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.service.GameActionsProcessor;
import com.zorg.zombies.service.UserUpdater;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zorg.zombies.Constants.*;
import static com.zorg.zombies.model.MoveDirectionX.*;
import static com.zorg.zombies.model.MoveDirectionY.*;
import static com.zorg.zombies.model.MoveDirectionZ.*;

@Getter
@Setter
public class User extends UserData {

    private final UserUpdater updater = new UserUpdater();
    private final UserMovementNotifier movementNotifier = new UserMovementNotifier(this);
    // exists for test purposes
    protected boolean movementNotifierEnabled = true;

    private int visibleDistance = 50; // dummy value for now

    private volatile boolean isMovingNorth;
    private volatile boolean isMovingSouth;
    private volatile boolean isMovingWest;
    private volatile boolean isMovingEast;
    private volatile boolean isMovingUp;
    private volatile boolean isMovingDown;

    private GameActionsProcessor processor;

    public User(String id) {
        super(id, new Coordinates(0, 0)); // todo: receive actual coordinates from somewhere...
    }

    public User(UserData userData) {
        super(userData.id, userData.coordinates);
    }

    public boolean isMoving() {
        return (isMovingNorth || isMovingSouth || isMovingWest || isMovingEast || isMovingUp || isMovingDown);
    }

    /**
     * Setting move without any checking!
     */
    public void setMoving(MoveDirection direction) {
        if (NORTH.equals(direction)) setMovingNorth(true);
        else if (EAST.equals(direction)) setMovingEast(true);
        else if (SOUTH.equals(direction)) setMovingSouth(true);
        else if (WEST.equals(direction)) setMovingWest(true);
        else if (UP.equals(direction)) setMovingUp(true);
        else if (DOWN.equals(direction)) setMovingDown(true);
    }

    public boolean isMoving(MoveDirection direction) {
        if (NORTH.equals(direction)) return isMovingNorth;
        else if (EAST.equals(direction)) return isMovingEast;
        else if (SOUTH.equals(direction)) return isMovingSouth;
        else if (WEST.equals(direction)) return isMovingWest;
        else if (UP.equals(direction)) return isMovingUp;
        else if (DOWN.equals(direction)) return isMovingDown;
        else return false;
    }

    /**
     * Setting move without any checking!
     */
    public void setStopMoving(MoveDirection stopMoving) {
        if (NORTH.equals(stopMoving)) setMovingNorth(false);
        else if (EAST.equals(stopMoving)) setMovingEast(false);
        else if (SOUTH.equals(stopMoving)) setMovingSouth(false);
        else if (WEST.equals(stopMoving)) setMovingWest(false);
        else if (UP.equals(stopMoving)) setMovingUp(false);
        else if (DOWN.equals(stopMoving)) setMovingDown(false);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    public UserChange act(UserMoveCommand command) {
        final UserChange userChange = updater.updateUserMove(this, command.getDirection());

        if (userChange.isUpdated()) {
            val change = new WorldChange(userChange);
            processor.getSubscriber().onNext(change);

            if (movementNotifierEnabled && movementNotifier.isDone()) movementNotifier.start();
        }

        return userChange;
    }

    public UserChange act(UserStopMoveCommand command) {
        final UserChange userChange = updater.updateUserStopMove(this, command.getDirection());

        if (userChange.isUpdated()) {
            val change = new WorldChange(userChange);
            processor.getSubscriber().onNext(change);
        }

        return userChange;
    }

    public void onDestroy() {
        movementNotifier.scheduledExecutor.shutdownNow();
    }

    public void makeMove() {
        final List<MoveDirection> moveDirections = getMovingDirections();

        for (MoveDirection direction : moveDirections) {
            coordinates.makeStep(direction);
        }

        processor.getSubscriber().onNext(new WorldChange(new UserPositionChange(this)));
    }

    protected List<MoveDirection> getMovingDirections() {
        List<MoveDirection> directions = new ArrayList<>();

        if (isMovingNorth) directions.add(NORTH);
        else if (isMovingSouth) directions.add(SOUTH);
        if (isMovingWest) directions.add(WEST);
        else if (isMovingEast) directions.add(EAST);
        if (isMovingUp) directions.add(UP);
        else if (isMovingDown) directions.add(DOWN);

        return Collections.unmodifiableList(directions);
    }

    public static class UserMovementNotifier {

        private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        private final User user;
        private volatile Future<?> movementFuture = CompletableFuture.completedFuture(null);

        public UserMovementNotifier(User user) {
            this.user = user;
        }

        public boolean isDone() {
            return movementFuture.isDone();
        }

        private void movementCommand() {
            if (user.isMoving()) {
                user.makeMove();
            } else {
                movementFuture.cancel(true); // todo: check if it also works with false
            }
        }

        public void start() {
            movementFuture = scheduledExecutor.scheduleWithFixedDelay(
                    // todo: ensure the scheduleAtFixedRate isn't better
                    this::movementCommand,
                    0,
                    HUMAN_WALK_DELAY_MS,
                    TimeUnit.MILLISECONDS
            );
        }
    }

}

