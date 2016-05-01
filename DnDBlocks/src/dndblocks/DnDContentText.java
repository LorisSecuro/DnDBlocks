package dndblocks;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;

public class DnDContentText extends DnDContent {

    private String text;

    public DnDContentText(DnDCell parentCell, String text) {
	super(parentCell);
	setContent(text);
    }

    @Override
    public void paint(GC gc) {
	if (text == null) {
	    return;
	}

	Point textExtent = gc.textExtent(text);

	Point cellSize = getCell().getSize();
	int clipMargin = 1;
	gc.setClipping(clipMargin, clipMargin, cellSize.x - clipMargin,
		cellSize.y - clipMargin);

	double posX = ((double) (cellSize.x)) / 2 - ((double) (textExtent.x))
		/ 2;
	double posY = ((double) (cellSize.y)) / 2 - ((double) (textExtent.y))
		/ 2;
	gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
	gc.drawText(text, (int) posX, (int) posY);
    }

    @Override
    public Object getContent() {
	return text;
    }

    public void setContent(String text) {
	this.text = text;
    }
}
