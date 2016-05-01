package dndblocks;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

public class DnDGrid extends Composite {

    private Map<DnDCell, Point> positionsInGrid = new HashMap<DnDCell, Point>();
    private DnDCell[][] cells;
    private int blockMargin = 0;
    private int blockSize = 14;

    public DnDGrid(Composite parent, int style, int numColumns, int numRows) {
	super(parent, style);

	// create grid cells
	cells = new DnDCell[numRows][numColumns];
	int locationY = blockMargin;
	for (int iy = 0; iy < cells.length; iy++) {
	    int locationX = blockMargin;
	    for (int ix = 0; ix < cells[iy].length; ix++) {
		cells[iy][ix] = new DnDCell(this, style);
		cells[iy][ix].setSize(blockSize, blockSize);
		cells[iy][ix].setLocation(locationX, locationY);

		positionsInGrid.put(cells[iy][ix], new Point(ix, iy));

		locationX += (blockSize + blockMargin);
	    }
	    locationY += (blockSize + blockMargin);
	}
    }

    public DnDCell getCell(int positionX, int positionY) {
	if (positionX < 0 || positionY < 0) {
	    return null;
	}
	if (positionY >= cells.length) {
	    return null;
	}
	if (positionX >= cells[positionY].length) {
	    return null;
	}
	return cells[positionY][positionX];
    }

    public Point getPosition(DnDCell cell) {
	return positionsInGrid.get(cell);
    }
}
