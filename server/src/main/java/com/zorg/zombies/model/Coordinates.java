package com.zorg.zombies.model;

import lombok.Data;
import lombok.val;

@Data
public class Coordinates {

    private int x;
    private int y;

    public Coordinates() {
        this(0, 0);
    }

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Data
    public static class Range {

        private final Coordinates topLeft;
        private final Coordinates bottomRight;

        public Range(Coordinates center, int range) {
            val userX = center.getX();
            val userY = center.getY();

            topLeft = new Coordinates(userX + range, userY + range);
            bottomRight = new Coordinates(userX - range, userY - range);
        }

        public int getX1() {
            return topLeft.getX();
        }

        public int getY1() {
            return topLeft.getY();
        }

        public int getX2() {
            return bottomRight.getX();
        }

        public int getY2() {
            return bottomRight.getY();
        }
    }
}
