package com.zorg.zombies.model;

import com.zorg.zombies.model.geometry.Direction;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

import static com.zorg.zombies.model.geometry.Direction.DOWN;
import static com.zorg.zombies.model.geometry.Direction.EAST;
import static com.zorg.zombies.model.geometry.Direction.NORTH;
import static com.zorg.zombies.model.geometry.Direction.SOUTH;
import static com.zorg.zombies.model.geometry.Direction.UP;
import static com.zorg.zombies.model.geometry.Direction.WEST;

@Data
public class Coordinates {

    private final AtomicInteger x;
    private final AtomicInteger y;
    private final AtomicInteger z;

    public Coordinates() {
        this(0, 0, 0);
    }

    public Coordinates(int x, int y) {
        this(x, y, 0);
    }

    public Coordinates(int x, int y, int z) {
        this.x = new AtomicInteger(x);
        this.y = new AtomicInteger(y);
        this.z = new AtomicInteger(z);
    }

    public int getX() {
        return x.get();
    }

    public int getY() {
        return y.get();
    }

    public int getZ() {
        return z.get();
    }

    public void makeStep(Direction direction) {
        if (NORTH.equals(direction)) y.incrementAndGet();
        else if (EAST.equals(direction)) x.incrementAndGet();
        else if (SOUTH.equals(direction)) y.decrementAndGet();
        else if (WEST.equals(direction)) x.decrementAndGet();
        else if (UP.equals(direction)) z.incrementAndGet();
        else if (DOWN.equals(direction)) z.decrementAndGet();
    }

}
