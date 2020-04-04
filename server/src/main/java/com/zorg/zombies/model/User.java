package com.zorg.zombies.model;

import com.zorg.zombies.change.UserChange;
import com.zorg.zombies.change.UserPositionChange;
import com.zorg.zombies.change.WorldChange;
import com.zorg.zombies.command.UserMoveCommand;
import com.zorg.zombies.command.UserStopMoveCommand;
import com.zorg.zombies.model.exception.WrongDirectionException;
import com.zorg.zombies.model.geometry.Direction;
import com.zorg.zombies.service.UserUpdater;
import com.zorg.zombies.service.UsersCommunicator;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.security.auth.Destroyable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.zorg.zombies.Constants.HUMAN_WALK_DELAY_MS;
import static com.zorg.zombies.model.geometry.Direction.DOWN;
import static com.zorg.zombies.model.geometry.Direction.EAST;
import static com.zorg.zombies.model.geometry.Direction.NORTH;
import static com.zorg.zombies.model.geometry.Direction.SOUTH;
import static com.zorg.zombies.model.geometry.Direction.UP;
import static com.zorg.zombies.model.geometry.Direction.WEST;

@Log4j2
public class User extends UserSubscriber implements Destroyable {

    private final UserUpdater updater = new UserUpdater();
    private final UserMovement movementNotifier = new UserMovement();
    private final UsersCommunicator usersCommunicator;

    // exists for test purposes
    @Setter
    private boolean movementNotifierEnabled = true;

    @Setter
    private volatile boolean wantToMoveNorth;
    @Setter
    private volatile boolean wantToMoveSouth;
    @Setter
    private volatile boolean wantToMoveWest;
    @Setter
    private volatile boolean wantToMoveEast;
    @Setter
    private volatile boolean wantToMoveUp;
    @Setter
    private volatile boolean wantToMoveDown;


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

        if (userChange.isUpdate()) {
            var change = new WorldChange<>(userChange);

            if (movementNotifierEnabled) movementNotifier.start();

            notifyUsers(change);
        }
    }

    private void notifyUsers(WorldChange userChange) {
        usersCommunicator.notifyUsers(userChange);
    }

    public void act(UserStopMoveCommand command) {
        final UserChange userChange = updater.updateUserStopMove(this, command.getDirection());

        if (userChange.isUpdate()) {
            var change = new WorldChange<>(userChange);
            notifyUsers(change);
        }
    }

    @Override
    public void destroy() {
        movementNotifier.destroy();
        usersCommunicator.unregister(id);
    }

    private void makeMove() {
        for (Direction direction : collectMovingDirections()) {
            coordinates.makeStep(direction);
        }

        notifyUsers(new WorldChange<>(new UserPositionChange(this)));
    }

    List<Direction> collectMovingDirections() {
        final List<Direction> directions = new ArrayList<>();

        if (isMovingNorth()) directions.add(NORTH);
        else if (isMovingSouth()) directions.add(SOUTH);
        if (isMovingWest()) directions.add(WEST);
        else if (isMovingEast()) directions.add(EAST);
        if (isMovingUp()) directions.add(UP);
        else if (isMovingDown()) directions.add(DOWN);

        return Collections.unmodifiableList(directions);
    }

    public boolean isMovingNorth() {
        return wantToMoveNorth && !wantToMoveSouth;
    }

    public boolean isMovingSouth() {
        return wantToMoveSouth && !wantToMoveNorth;
    }

    public boolean isMovingWest() {
        return wantToMoveWest && !wantToMoveEast;
    }

    public boolean isMovingEast() {
        return wantToMoveEast && !wantToMoveWest;
    }

    public boolean isMovingUp() {
        return wantToMoveUp && !wantToMoveDown;
    }

    public boolean isMovingDown() {
        return wantToMoveDown && !wantToMoveUp;
    }

    public boolean isMoving() {
        return isMovingNorth()
                || isMovingSouth()
                || isMovingWest()
                || isMovingEast()
                || isMovingUp()
                || isMovingDown();
    }

    public boolean isMoving(Direction direction) {
        switch (direction) {
            case NORTH:
                return isMovingNorth();
            case EAST:
                return isMovingEast();
            case SOUTH:
                return isMovingSouth();
            case WEST:
                return isMovingWest();
            case UP:
                return isMovingUp();
            case DOWN:
                return isMovingDown();
            default:
                throw new WrongDirectionException(direction);
        }
    }


    private class UserMovement implements Destroyable {

        private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        private volatile Future<?> movementFuture = CompletableFuture.completedFuture(null);

        private void movementCommand() {
            if (isMoving()) {
                makeMove();
            }
        }

        void start() {
            if (!movementFuture.isDone()) {
                return;
            }

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

