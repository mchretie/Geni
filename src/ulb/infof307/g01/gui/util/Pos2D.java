package ulb.infof307.g01.gui.util;

import java.util.Objects;

/**
 * Serves only as a mean to encapsulate both values
 */
public class Pos2D {
    public int col;
    public int row;

    public Pos2D(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public Pos2D(Pos2D pos) {
        this.col = pos.col;
        this.row = pos.row;
    }

    @Override
    public String toString() {
        return "Pos2D{" +
                "col=" + col +
                ", row=" + row +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos2D that = (Pos2D) o;
        return col == that.col && row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }
}
