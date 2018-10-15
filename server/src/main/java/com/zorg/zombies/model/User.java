package com.zorg.zombies.model;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserPositionChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.service.UserUpdater;
import com.zorg.zombies.service.UsersCommunicator;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import javax.security.auth.Destroyable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zorg.zombies.Constants.*;
import static com.zorg.zombies.util.MovementAndDirections.*;

@Getter
@Setter
public class User extends UserSubscriber implements Destroyable {

    private final UserUpdater updater = new UserUpdater();
    private final UserMovement movementNotifier = new UserMovement();
    private final UsersCommunicator usersCommunicator;

    // exists for test purposes
    protected boolean movementNotifierEnabled = true;

    private volatile boolean isMovingNorth;
    private volatile boolean isMovingSouth;
    private volatile boolean isMovingWest;
    private volatile boolean isMovingEast;
    private volatile boolean isMovingUp;
    private volatile boolean isMovingDown;

    public User(String id, Coordinates coordinates, UsersCommunicator usersCommunicator) {
        super(id, coordinates);
        this.usersCommunicator = usersCommunicator;
    }

    public User(String id, UsersCommunicator usersCommunicator) {
        this(id, new Coordinates(0, 0), usersCommunicator);
    }

    public User(UserData userData, UsersCommunicator usersCommunicator) {
        this(userData.id, userData.coordinates, usersCommunicator);
    }

    public void notifyJoining() {
        usersCommunicator.register(this);
    }

    public void act(UserMoveCommand command) {
        final UserChange userChange = updater.updateUserMove(this, command.getDirection());

        if (userChange.isUpdated()) {
            val change = new WorldChange<>(userChange);

            if (movementNotifierEnabled) movementNotifier.start();

            notifyUsers(change);
        }
    }

    private void notifyUsers(WorldChange userChange) {
        usersCommunicator.notifyUsers(userChange);
    }

    public void act(UserStopMoveCommand command) {
        final UserChange userChange = updater.updateUserStopMove(this, command.getDirection());

        if (userChange.isUpdated()) {
            val change = new WorldChange<>(userChange);
            notifyUsers(change);
        }
    }

    @Override
    public void destroy() {
        movementNotifier.destroy();
        usersCommunicator.unregister(id);
    }

    private void makeMove() {
        final List<MoveDirection> moveDirections = collectMovingDirections(this);

        for (MoveDirection direction : moveDirections) {
            coordinates.makeStep(direction);
        }

        notifyUsers(new WorldChange<>(new UserPositionChange(this)));
    }

    class UserMovement implements Destroyable {

        private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        private volatile Future<?> movementFuture = CompletableFuture.completedFuture(null);

        private void movementCommand() {
            if (isMoving(User.this)) makeMove();
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

        @Override
        public void destroy() {
            movementFuture.cancel(true);
            scheduledExecutor.shutdownNow();
        }
    }

}

