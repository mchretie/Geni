package ulb.infof307.g01.gui.util;

import java.util.Objects;

/**
 * Serves only as a mean to encapsulate both values
 */
public class Position2D {
    public int x;
    public int y;

    public Position2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position2D(Position2D pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    @Override
    public String toString() {
        return "Position2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position2D that = (Position2D) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
