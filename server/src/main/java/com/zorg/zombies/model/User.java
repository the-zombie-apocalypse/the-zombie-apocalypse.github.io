package com.zorg.zombies.model;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserPositionChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.service.UserUpdater;
import com.zorg.zombies.service.UsersCommunicator;
import com.zorg.zombies.util.MovementAndDirections;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zorg.zombies.Constants.*;

@Getter
@Setter
public class User extends UserSubscriber {

    private final UserUpdater updater = new UserUpdater();
    private final UserMovementNotifier movementNotifier = new UserMovementNotifier();

    // exists for test purposes
    protected boolean movementNotifierEnabled = true;

    private int visibleDistance = 50; // dummy value for now

    private volatile boolean isMovingNorth;
    private volatile boolean isMovingSouth;
    private volatile boolean isMovingWest;
    private volatile boolean isMovingEast;
    private volatile boolean isMovingUp;
    private volatile boolean isMovingDown;

    public User(String id, UsersCommunicator usersCommunicator) {
        super(id, new Coordinates(0, 0), usersCommunicator);
    }

    public User(UserData userData, UsersCommunicator usersCommunicator) {
        super(userData.id, userData.coordinates, usersCommunicator);
    }

    public void notifyJoining() {
        usersCommunicator.register(this);
    }

    public boolean isMoving() {
        return MovementAndDirections.isMoving(this);
    }

    /**
     * Setting move without any checking!
     */
    public void setMoving(MoveDirection direction) {
        MovementAndDirections.setMoving(direction, this);
    }

    public boolean isMoving(MoveDirection direction) {
        return MovementAndDirections.isMoving(direction, this);
    }

    /**
     * Setting move stop without any checking!
     */
    public void setStopMoving(MoveDirection stopMoving) {
        MovementAndDirections.setStopMoving(stopMoving, this);
    }

    public void act(UserMoveCommand command) {
        final UserChange userChange = updater.updateUserMove(this, command.getDirection());

        if (userChange.isUpdated()) {
            val change = new WorldChange<>(userChange);

            if (movementNotifierEnabled) movementNotifier.start();

            notifyUsers(change);
        }
    }

    void notifyUsers(WorldChange userChange) {
        usersCommunicator.notifyUsers(userChange);
    }

    public void act(UserStopMoveCommand command) {
        final UserChange userChange = updater.updateUserStopMove(this, command.getDirection());

        if (userChange.isUpdated()) {
            val change = new WorldChange<>(userChange);
            notifyUsers(change);
        }
    }

    public void onDestroy() {
        movementNotifier.scheduledExecutor.shutdownNow();
    }

    public void makeMove() {
        final List<MoveDirection> moveDirections = getMovingDirections();

        for (MoveDirection direction : moveDirections) {
            coordinates.makeStep(direction);
        }

        notifyUsers(new WorldChange<>(new UserPositionChange(this)));
    }

    protected List<MoveDirection> getMovingDirections() {
        return MovementAndDirections.collectMovingDirections(this);
    }

    class UserMovementNotifier {

        private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        private volatile Future<?> movementFuture = CompletableFuture.completedFuture(null);

        private void movementCommand() {
            if (isMoving()) makeMove();
            else movementFuture.cancel(false);
        }

        void start() {
            if (!movementFuture.isDone()) return;

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

