package ulb.infof307.g01.gui.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class GridPosIterator implements Iterator<Pos2D> {
    
    Pos2D currentPosition;

    final int colCount;
    final int rowCount;

    int index;
    final int maxIndex;
    
    public GridPosIterator(int colCount, int rowCount) {
        if (colCount <= 0 || rowCount <= 0)
            throw new IllegalArgumentException("Counts must be positive");

        this.currentPosition = new Pos2D(0, 0);
        this.colCount = colCount;
        this.rowCount = rowCount;
        this.index = -1;  // to have (0, 0) as first element
        this.maxIndex = colCount * rowCount;
    }
    
    @Override
    public boolean hasNext() {
        return index < maxIndex-1;
    }

    @Override
    public Pos2D next() {
        if (!hasNext())
            throw new NoSuchElementException("Reached past end of iterator");

        index++;
        currentPosition.col = index % colCount;
        currentPosition.row = rowCount == 1 ? 0 : index / colCount;
        return new Pos2D(currentPosition);
    }
}
