package ulb.infof307.g01.gui.util;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GridPosIteratorTest {

     <T>
     void assertIteratorsEquals(Iterator<T> fst, Iterator<T> snd) {
         while (fst.hasNext() && snd.hasNext()) {
             assertEquals(fst.next(), snd.next());
         }
         assertEquals(fst.hasNext(), snd.hasNext());
     }

     @Test
     void GridPosIterator_InvalidArgs_Throws() {
         assertThrows(IllegalArgumentException.class, () -> new GridPosIterator(-1, 5));
         assertThrows(IllegalArgumentException.class, () -> new GridPosIterator(5, -1));
         assertThrows(IllegalArgumentException.class, () -> new GridPosIterator(0, 5));
         assertThrows(IllegalArgumentException.class, () -> new GridPosIterator(5, 0));
     }

    @Test
    void iterate_OneCellGrid_NoThrow() {
        var expected = List.of(
                new Pos2D(0, 0)
        ).iterator();
        var actual = new GridPosIterator(1, 1);

        assertIteratorsEquals(expected, actual);
    }

    @Test
    void iterate_SquareGrid_NoThrow() {
        var expected = List.of(
                new Pos2D(0, 0),
                new Pos2D(1, 0),
                new Pos2D(0, 1),
                new Pos2D(1, 1)
        ).iterator();
        var actual = new GridPosIterator(2, 2);

        assertIteratorsEquals(expected, actual);
    }

    @Test
    void iterate_RowGrid_NoThrow() {
        var expected = List.of(
                new Pos2D(0, 0),
                new Pos2D(1, 0),
                new Pos2D(2, 0),
                new Pos2D(3, 0)
        ).iterator();
        var actual = new GridPosIterator(4, 1);

        assertIteratorsEquals(expected, actual);
    }

    @Test
    void iterate_ColGrid_NoThrow() {
        var expected = List.of(
                new Pos2D(0, 0),
                new Pos2D(0, 1),
                new Pos2D(0, 2),
                new Pos2D(0, 3)
        ).iterator();
        var actual = new GridPosIterator(1, 4);

        assertIteratorsEquals(expected, actual);
    }

    @Test
    void iterate_DiffColAndRow_NoThrow() {
        var expected = List.of(
                new Pos2D(0, 0),
                new Pos2D(1, 0),
                new Pos2D(2, 0),
                new Pos2D(0, 1),
                new Pos2D(1, 1),
                new Pos2D(2, 1)
        ).iterator();
        var actual = new GridPosIterator(3, 2);

        assertIteratorsEquals(expected, actual);
    }
}