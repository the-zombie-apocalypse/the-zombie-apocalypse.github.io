package com.zorg.zombies.model.geometry;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Direction {

    EAST(Dimension.X),
    WEST(Dimension.X),
    NORTH(Dimension.Y),
    SOUTH(Dimension.Y),
    UP(Dimension.Z),
    DOWN(Dimension.Z);


    private final Dimension dimension;

    public Direction getOpposite() {
        switch (this) {
            case EAST:
                return WEST;
            case WEST:
                return EAST;
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            default:
                throw new RuntimeException("Wrong direction " + this);
        }
    }
}
